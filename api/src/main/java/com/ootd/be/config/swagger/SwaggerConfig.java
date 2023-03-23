package com.ootd.be.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI().servers(List.of(new Server().url("http://localhost/api")));
//    }

    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("auth")
                .displayName("계정")
                .pathsToMatch("/auth/**")
                             .build();
    }

    @Bean
    public GroupedOpenApi feedGroup() {
        return GroupedOpenApi.builder()
                             .group("feed")
                             .displayName("피드")
                             .pathsToMatch("/feed/**")
                             .build();
    }

    @Bean
    public GroupedOpenApi confirmGroup() {
        return GroupedOpenApi.builder()
                             .group("confirm")
                             .displayName("컨펌")
                             .pathsToMatch("/confirm/**")
                             .build();
    }

    @Bean
    public GroupedOpenApi myGroup() {
        return GroupedOpenApi.builder()
                             .group("my")
                             .displayName("마이페이지")
                             .pathsToMatch("/my/**")
                             .build();
    }

    @Bean
    public GroupedOpenApi musinsaGroup() {
        return GroupedOpenApi.builder()
                             .group("musinsa")
                             .displayName("무신사")
                             .pathsToMatch("/m-api/**")
                             .build();
    }


    @Bean
    public OpenAPI info() {
        return new OpenAPI()
                .info(new Info().title("오늘의옷장 - api").description("오늘의 옷장 api 문서").version("1.0.0"));
    }

}
