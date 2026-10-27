package com.example.directory.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.directory.application.port.out.CredentialVerifier;
import com.example.directory.application.port.out.UserDirectory;
import com.example.directory.domain.InvalidCredentialsException;
import com.example.directory.domain.User;
import com.example.directory.domain.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ApplicationServicesTest {

    private final User alice = new User("alice", "Alice Anderson", "Anderson", "alice@example.com");

    private final UserDirectory directory = new UserDirectory() {
        public Optional<User> findByUsername(String u) { return u.equals("alice") ? Optional.of(alice) : Optional.empty(); }
        public List<User> findByLastName(String l) { return l.equals("Anderson") ? List.of(alice) : List.of(); }
        public List<String> findGroupNames(String u) { return List.of("developers"); }
    };

    private final CredentialVerifier verifier = (u, p) -> u.equals("alice") && p.equals("alicepw");

    private final UserQueryService query = new UserQueryService(directory);
    private final AuthenticationService auth = new AuthenticationService(verifier, directory);

    @Test
    void findsUser() {
        assertThat(query.findByUsername(" alice ")).isEqualTo(alice);
    }

    @Test
    void unknownUserThrows() {
        assertThatThrownBy(() -> query.findByUsername("nobody")).isInstanceOf(UserNotFoundException.class);
        assertThatThrownBy(() -> query.getGroups("nobody")).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void blankInputIsRejected() {
        assertThatThrownBy(() -> query.searchByLastName(" ")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void authenticatesWithCorrectPassword() {
        assertThat(auth.authenticate("alice", "alicepw")).isEqualTo(alice);
    }

    @Test
    void rejectsWrongOrEmptyPassword() {
        assertThatThrownBy(() -> auth.authenticate("alice", "falsch")).isInstanceOf(InvalidCredentialsException.class);
        assertThatThrownBy(() -> auth.authenticate("alice", "")).isInstanceOf(InvalidCredentialsException.class);
        assertThatThrownBy(() -> auth.authenticate("alice", null)).isInstanceOf(InvalidCredentialsException.class);
    }
}