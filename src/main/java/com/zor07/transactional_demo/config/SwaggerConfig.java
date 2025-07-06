package com.zor07.transactional_demo.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transaction Demo API")
                        .version("1.0")
                        .description("API для демонстрации работы @Transactional в Spring")
                        .contact(new Contact()
                                .name("Your Java Mentor")
                                .url("https://t.me/your_java_mentor"))
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Дополнительная документация")
                        .url("https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html"));
    }

}
