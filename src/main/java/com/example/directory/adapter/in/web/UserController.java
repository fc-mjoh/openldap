package com.example.directory.adapter.in.web;

import com.example.directory.application.port.in.FindUserUseCase;
import com.example.directory.application.port.in.GetUserGroupsUseCase;
import com.example.directory.application.port.in.SearchUsersUseCase;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
class UserController {

    private final FindUserUseCase find;
    private final SearchUsersUseCase search;
    private final GetUserGroupsUseCase groups;

    UserController(FindUserUseCase find, SearchUsersUseCase search, GetUserGroupsUseCase groups) {
        this.find = find;
        this.search = search;
        this.groups = groups;
    }

    @GetMapping("/{username}")
    UserResponse get(@PathVariable String username) {
        return UserResponse.from(find.findByUsername(username));
    }

    @GetMapping
    List<UserResponse> search(@RequestParam String lastName) {
        return search.searchByLastName(lastName).stream().map(UserResponse::from).toList();
    }

    @GetMapping("/{username}/groups")
    List<String> groups(@PathVariable String username) {
        return groups.getGroups(username);
    }
}