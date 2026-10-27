package com.example.directory.application.port.out;

public interface CredentialVerifier {
    boolean verify(String username, String password);
}