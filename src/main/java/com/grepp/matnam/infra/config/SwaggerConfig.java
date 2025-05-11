package com.grepp.matnam.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    //http://localhost:8080/swagger-ui/index.html
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(info());
    }

    private Info info() {
        return new Info()
            .title("맛남 API")
            .description("프로그래머스 데브코스 2차 프로젝트 맛남 API 문서입니다.")
            .version("1.0");
    }
}
