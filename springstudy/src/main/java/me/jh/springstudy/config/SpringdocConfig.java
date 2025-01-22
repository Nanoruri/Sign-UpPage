package me.jh.springstudy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Springstudy API")
                        .version("v1")
                        .description("여기에 명시된 API는 Springstudy 프로젝트의 API 명세입니다."));
    }
}
