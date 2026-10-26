package com.example.directory.adapter.in.web;

import com.example.directory.application.port.in.FindUserUseCase;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class MeController {

    record MeResponse(UserResponse user, List<String> roles) {}

    private final FindUserUseCase find;

    MeController(FindUserUseCase find) {
        this.find = find;
    }

    @GetMapping("/api/me")
    MeResponse me(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).sorted().toList();
        return new MeResponse(UserResponse.from(find.findByUsername(authentication.getName())), roles);
    }
}