package com.example.directory.domain;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Benutzername oder Passwort falsch");
    }
}
