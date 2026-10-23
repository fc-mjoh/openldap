package com.example.directory.application;

import com.example.directory.application.port.in.AuthenticateUseCase;
import com.example.directory.application.port.out.CredentialVerifier;
import com.example.directory.application.port.out.UserDirectory;
import com.example.directory.domain.InvalidCredentialsException;
import com.example.directory.domain.User;

public class AuthenticationService implements AuthenticateUseCase {

    private final CredentialVerifier verifier;
    private final UserDirectory directory;

    public AuthenticationService(CredentialVerifier verifier, UserDirectory directory) {
        this.verifier = verifier;
        this.directory = directory;
    }

    @Override
    public User authenticate(String username, String password) {
        String name = UserQueryService.requireText(username, "username");
        // Wichtig: Ein leeres Passwort ist bei manchen LDAP-Servern ein anonymer Bind und
        // würde "erfolgreich" sein. Deshalb hier im Kern hart abweisen.
        if (password == null || password.isEmpty() || !verifier.verify(name, password)) {
            throw new InvalidCredentialsException();
        }
        return directory.findByUsername(name).orElseThrow(InvalidCredentialsException::new);
    }
}
