package com.example.directory.adapter.out.ldap;

import com.example.directory.application.port.out.CredentialVerifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

/**
 * Sucht den Benutzer und versucht dann einen Bind mit dessen DN und dem Passwort.
 */
@Component
class LdapCredentialVerifier implements CredentialVerifier {

    private final LdapTemplate ldap;

    LdapCredentialVerifier(LdapTemplate ldap) {
        this.ldap = ldap;
    }

    @Override
    public boolean verify(String username, String password) {
        try {
            ldap.authenticate(LdapUserDirectory.personQuery("uid", username), password);
            return true;
        } catch (EmptyResultDataAccessException | AuthenticationException e) {
            // Benutzer unbekannt oder Passwort falsch. Beides gleich behandeln,
            // damit niemand per Login herausfinden kann, welche Benutzer existieren.
            return false;
        }
        // Alle anderen Exceptions (z. B. LDAP nicht erreichbar, mehrdeutige uid)
        // bewusst nicht abfangen: Das sind Betriebsfehler, keine falschen Zugangsdaten.
    }
}
