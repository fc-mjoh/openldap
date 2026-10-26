package com.example.directory.adapter.in.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Primärer Adapter: Authentifizierung und Autorisierung der eingehenden HTTP-Anfragen.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig {

    /**
     * Gruppen unter ou=groups (groupOfNames) -> Rollen ROLE_<CN in Großbuchstaben>.
     */
    @Bean
    LdapAuthoritiesPopulator ldapAuthorities(BaseLdapPathContextSource contextSource) {
        DefaultLdapAuthoritiesPopulator populator =
                new DefaultLdapAuthoritiesPopulator(contextSource, "ou=groups");
        populator.setGroupSearchFilter("(member={0})"); // {0} = voller DN des Benutzers
        return populator;
    }

    /**
     * Bind-Authentifizierung: Der LDAP-Server prüft das Passwort.
     */
    @Bean
    AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource,
                                                LdapAuthoritiesPopulator authorities) {
        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserDnPatterns("uid={0},ou=people");
        factory.setLdapAuthoritiesPopulator(authorities);
        return factory.createAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/me").authenticated()
                        .requestMatchers("/api/users/*/groups").hasRole("ADMINS")
                        .requestMatchers("/api/users/**").hasAnyRole("DEVELOPERS", "ADMINS")
                        .anyRequest().authenticated())
                .formLogin(withDefaults())   // Login-Seite unter /login
                .logout(withDefaults())      // POST /logout
                .httpBasic(withDefaults());  // optional: curl und API-Clients funktionieren weiter
        // Keine STATELESS-Policy und kein csrf.disable(): Sessions und CSRF-Schutz bleiben aktiv.
        return http.build();
    }
}