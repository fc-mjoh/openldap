package com.example.directory.application.port.in;

import com.example.directory.domain.User;

public interface AuthenticateUseCase {
    /** @throws com.example.directory.domain.InvalidCredentialsException bei falschen Zugangsdaten */
    User authenticate(String username, String password);
}
