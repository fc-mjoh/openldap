package com.example.directory.application.port.in;

import com.example.directory.domain.User;

public interface FindUserUseCase {
    User findByUsername(String username);
}