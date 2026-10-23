package com.example.directory.application.port.in;

import java.util.List;

public interface GetUserGroupsUseCase {
    List<String> getGroups(String username);
}
