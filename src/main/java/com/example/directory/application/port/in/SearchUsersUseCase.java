package com.example.directory.application.port.in;

import com.example.directory.domain.User;

import java.util.List;

public interface SearchUsersUseCase {
    List<User> searchByLastName(String lastName);
}