package com.project.auth.auth_backend.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
    info = @Info(
            title = "Auth Application",
            description = "Auth App use for any application",
            contact = @Contact(
                name = "Mohak Mittal",
                url = "https://mohakmittal.com",
                email = "mohak012@gmail.com"
            ),
            version = "1.0",
            summary = "This app is very useful if you dont want to create auth app from scratch"
    ),
    security = {
        @SecurityRequirement(
            name = "bearerAuth"
        )
    }
)

@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",  //autorization: bearer
    bearerFormat = "JWT" //format of token
)
public class ApiDocConfig {
}
