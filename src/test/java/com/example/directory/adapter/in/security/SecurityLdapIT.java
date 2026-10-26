package com.example.directory.adapter.in.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

@SpringBootTest
class SecurityLdapIT {

    @Autowired
    AuthenticationManager authenticationManager;

    private Authentication login(String user, String password) {
        return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(user, password));
    }

    private static java.util.List<String> roles(Authentication a) {
        return a.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(name -> name.startsWith("ROLE_"))
                .toList();
    }

    @Test
    void aliceGetsRolesFromBothGroups() {
        Authentication auth = login("alice", "alicepw");
        assertThat(auth.isAuthenticated()).isTrue();
        assertThat(roles(auth)).containsExactlyInAnyOrder("ROLE_DEVELOPERS", "ROLE_ADMINS");
    }

    @Test
    void bobIsOnlyDeveloper() {
        assertThat(roles(login("bob", "bobpw"))).containsExactly("ROLE_DEVELOPERS");
    }

    @Test
    void carolHasNoRoles() {
        assertThat(roles(login("carol", "carolpw"))).isEmpty();
    }

    @Test
    void wrongPasswordIsRejected() {
        assertThatThrownBy(() -> login("alice", "falsch")).isInstanceOf(AuthenticationException.class);
    }

    @Test
    void unknownUserIsRejected() {
        assertThatThrownBy(() -> login("nobody", "x")).isInstanceOf(AuthenticationException.class);
    }
}