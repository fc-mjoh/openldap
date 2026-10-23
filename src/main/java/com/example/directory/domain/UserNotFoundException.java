package com.example.directory.domain;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Benutzer nicht gefunden: " + username);
    }
}