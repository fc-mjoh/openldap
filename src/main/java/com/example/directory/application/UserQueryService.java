package com.example.directory.application;

import com.example.directory.application.port.in.FindUserUseCase;
import com.example.directory.application.port.in.GetUserGroupsUseCase;
import com.example.directory.application.port.in.SearchUsersUseCase;
import com.example.directory.application.port.out.UserDirectory;
import com.example.directory.domain.User;
import com.example.directory.domain.UserNotFoundException;

import java.util.List;

/** Bewusst ohne Spring-Annotationen, die Verdrahtung passiert in config/. */
public class UserQueryService implements FindUserUseCase, SearchUsersUseCase, GetUserGroupsUseCase {

    private final UserDirectory directory;

    public UserQueryService(UserDirectory directory) {
        this.directory = directory;
    }

    @Override
    public User findByUsername(String username) {
        String name = requireText(username, "username");
        return directory.findByUsername(name).orElseThrow(() -> new UserNotFoundException(name));
    }

    @Override
    public List<User> searchByLastName(String lastName) {
        return directory.findByLastName(requireText(lastName, "lastName"));
    }

    @Override
    public List<String> getGroups(String username) {
        User user = findByUsername(username); // 404 für unbekannte Benutzer
        return directory.findGroupNames(user.username());
    }

    static String requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " darf nicht leer sein");
        }
        return value.trim();
    }
}