package com.mediShop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Medical Inventory API")
                        .version("1.0.0")
                        .description("API documentation for Medical Inventory Management System"))
                .servers(List.of(
                        new Server().url("http://localhost:8081/medicines").description("Local server")
                ));
    }
}
