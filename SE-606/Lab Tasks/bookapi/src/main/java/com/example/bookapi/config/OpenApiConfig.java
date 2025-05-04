package com.example.bookapi.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book API")
                        .version("1.0.0")
                        .description("A Clean-Architecture Spring Boot CRUD API"));
    }
}
