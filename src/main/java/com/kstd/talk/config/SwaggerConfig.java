package com.kstd.talk.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("강연 신청 플랫폼")
                        .description("Swagger UI")
                        .version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi talkApi() {
        return GroupedOpenApi.builder()
                .group("talk")
                .pathsToMatch("/api/**")
                .build();
    }
}

