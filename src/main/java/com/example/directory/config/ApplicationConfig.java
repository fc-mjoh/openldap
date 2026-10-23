package com.example.directory.config;

import com.example.directory.application.AuthenticationService;
import com.example.directory.application.UserQueryService;
import com.example.directory.application.port.out.CredentialVerifier;
import com.example.directory.application.port.out.UserDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Einzige Stelle, an der der Kern zu Spring-Beans wird. */
@Configuration
class ApplicationConfig {

    @Bean
    UserQueryService userQueryService(UserDirectory directory) {
        return new UserQueryService(directory);
    }

    @Bean
    AuthenticationService authenticationService(CredentialVerifier verifier, UserDirectory directory) {
        return new AuthenticationService(verifier, directory);
    }
}
