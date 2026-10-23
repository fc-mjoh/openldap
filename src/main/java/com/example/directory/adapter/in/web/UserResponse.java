package com.example.directory.adapter.in.web;

import com.example.directory.domain.User;

record UserResponse(String username, String fullName, String email) {
    static UserResponse from(User u) {
        return new UserResponse(u.username(), u.fullName(), u.email());
    }
}
