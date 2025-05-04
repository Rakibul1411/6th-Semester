package com.example.usermanagement.config;

import com.example.usermanagement.application.RoleService;
import com.example.usermanagement.application.UserService;
import com.example.usermanagement.application.interfaces.RoleRepository;
import com.example.usermanagement.application.interfaces.UserRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for application beans.
 */
@Configuration
public class BeanConfig {

    /**
     * Create UserService bean.
     */
    @Bean
    public UserService userService(UserRepository userRepository, RoleRepository roleRepository) {
        return new UserService(userRepository, roleRepository);
    }

    /**
     * Create RoleService bean.
     */
    @Bean
    public RoleService roleService(RoleRepository roleRepository) {
        return new RoleService(roleRepository);
    }

    /**
     * Configure OpenAPI/Swagger documentation.
     */
    @Bean
    public OpenAPI userManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Management API")
                        .description("REST API for managing users and roles following Clean Architecture principles")
                        .version("1.0.0"));
    }
}