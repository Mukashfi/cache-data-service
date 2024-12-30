package com.aztech.cds.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "My API",
                version = "1.0",
                description = "This is a sample API documentation",
                contact = @Contact(name = "Hassan-Elmukashfi", email = "elmukashfi.hassan@gmail.com")
        )
)
public class SwaggerConfig {
}
