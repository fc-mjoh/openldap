package com.example.directory.application.port.out;

import com.example.directory.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserDirectory {
    Optional<User> findByUsername(String username);
    List<User> findByLastName(String lastName);
    List<String> findGroupNames(String username);
}