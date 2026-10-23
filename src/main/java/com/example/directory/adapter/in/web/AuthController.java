package com.example.directory.adapter.in.web;

import com.example.directory.application.port.in.AuthenticateUseCase;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo: prüft nur Zugangsdaten, stellt weder Session noch Token aus.
 */
@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final AuthenticateUseCase authenticate;

    AuthController(AuthenticateUseCase authenticate) {
        this.authenticate = authenticate;
    }

    @PostMapping("/verify")
    UserResponse verify(@RequestBody LoginRequest request) {
        return UserResponse.from(authenticate.authenticate(request.username(), request.password()));
    }
}