package com.ootd.be.config.swagger;

import com.ootd.be.config.security.SecurityConfig;
import com.ootd.be.config.security.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.ActuatorOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    public static final String SCHEME = "JWT-SCHEME";

    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("auth")
                .displayName("계정")
                .pathsToMatch("/auth/**")
                .addOperationCustomizer((operation, handlerMethod) -> operation.security(List.of(new SecurityRequirement().addList(SCHEME))))
                .build();
    }

    @Bean
    public GroupedOpenApi feedGroup() {
        return GroupedOpenApi.builder()
                .group("feed")
                .displayName("피드")
                .pathsToMatch("/feed/**")
                .addOperationCustomizer((operation, handlerMethod) -> operation.security(List.of(new SecurityRequirement().addList(SCHEME))))
                .build();
    }

    @Bean
    public GroupedOpenApi confirmGroup() {
        return GroupedOpenApi.builder()
                .group("confirm")
                .displayName("컨펌")
                .pathsToMatch("/confirm/**")
                .addOperationCustomizer((operation, handlerMethod) -> operation.security(List.of(new SecurityRequirement().addList(SCHEME))))
                .build();
    }

    @Bean
    public GroupedOpenApi myGroup() {
        return GroupedOpenApi.builder()
                .group("my")
                .displayName("마이페이지")
                .pathsToMatch("/my/**")
                .addOperationCustomizer((operation, handlerMethod) -> operation.security(List.of(new SecurityRequirement().addList(SCHEME))))
                .build();
    }

    @Bean
    public GroupedOpenApi musinsaGroup() {
        return GroupedOpenApi.builder()
                .group("musinsa")
                .displayName("무신사")
                .pathsToMatch("/m-api/**")
                .addOperationCustomizer((operation, handlerMethod) -> operation.security(List.of(new SecurityRequirement().addList(SCHEME))))
                .build();
    }

    @Bean
    public OpenAPI info() {
        Info info = new Info()
                .title("오늘의옷장 - api")
                .description("오늘의 옷장 api 문서")
                .version("1.0.0");

        Components components = new Components()
                .addSecuritySchemes(SCHEME, new SecurityScheme().type(SecurityScheme.Type.HTTP).in(SecurityScheme.In.HEADER).name(JwtTokenProvider.AUTHORIZATION_HEADER).scheme("Bearer").bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .components(components);
    }

}
