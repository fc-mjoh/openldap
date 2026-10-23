package com.example.directory.adapter.out.ldap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import com.example.directory.application.port.out.UserDirectory;
import com.example.directory.domain.User;
import java.util.List;
import java.util.Optional;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Component;

@Component
class LdapUserDirectory implements UserDirectory {

    private static final String PEOPLE = "ou=people";
    private static final String GROUPS = "ou=groups";

    private static final AttributesMapper<User> USER_MAPPER = attrs -> new User(
            value(attrs, "uid"), value(attrs, "cn"), value(attrs, "sn"), value(attrs, "mail"));

    private static final AttributesMapper<String> GROUP_NAME_MAPPER = attrs -> value(attrs, "cn");

    private final LdapTemplate ldap;

    LdapUserDirectory(LdapTemplate ldap) {
        this.ldap = ldap;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return ldap.search(personQuery("uid", username), USER_MAPPER).stream().findFirst();
    }

    @Override
    public List<User> findByLastName(String lastName) {
        return ldap.search(personQuery("sn", lastName), USER_MAPPER);
    }

    @Override
    public List<String> findGroupNames(String username) {
        // Vollständigen DN des Benutzers ermitteln (z. B. uid=alice,ou=people,dc=example,dc=com)
        List<String> dns = ldap.search(personQuery("uid", username),
                (ContextMapper<String>) ctx -> ((DirContextOperations) ctx).getNameInNamespace());
        if (dns.isEmpty()) {
            return List.of();
        }
        LdapQuery groups = query()
                .base(GROUPS)
                .where("objectclass").is("groupOfNames")
                .and("member").is(dns.get(0));
        return ldap.search(groups, GROUP_NAME_MAPPER);
    }

    /** Der LdapQueryBuilder maskiert Sonderzeichen im Wert und verhindert LDAP-Injection. */
    static LdapQuery personQuery(String attribute, String value) {
        return query()
                .base(PEOPLE)
                .where("objectclass").is("inetOrgPerson")
                .and(attribute).is(value);
    }

    private static String value(Attributes attrs, String name) throws NamingException {
        Attribute a = attrs.get(name);
        return a == null ? null : (String) a.get();
    }
}