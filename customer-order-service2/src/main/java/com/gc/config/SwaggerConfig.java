package com.gc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(
        		new Info().title("Customer and Order Application").description("By Proounce"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

}
