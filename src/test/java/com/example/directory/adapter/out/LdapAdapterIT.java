package com.example.directory.adapter.out.ldap;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.directory.application.port.in.FindUserUseCase;
import com.example.directory.application.port.in.GetUserGroupsUseCase;
import com.example.directory.application.port.in.SearchUsersUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LdapAdapterIT {

    @Autowired FindUserUseCase find;
    @Autowired SearchUsersUseCase search;
    @Autowired GetUserGroupsUseCase groups;

    @Test
    void findsAlice() {
        assertThat(find.findByUsername("alice").email()).isEqualTo("alice@example.com");
    }

    @Test
    void searchesByLastName() {
        assertThat(search.searchByLastName("Anderson")).hasSize(2);
    }

    @Test
    void wildcardIsEscaped() {
        assertThat(search.searchByLastName("*")).isEmpty();
    }

    @Test
    void resolvesGroups() {
        assertThat(groups.getGroups("alice")).containsExactlyInAnyOrder("developers", "admins");
        assertThat(groups.getGroups("bob")).containsExactly("developers");
        assertThat(groups.getGroups("carol")).isEmpty();
    }
}
