package com.capitole.technicaltest.catalog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI apiInfo() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Capitole Technical Test - Product Catalog API")
                .version("v1")
                .description(
                    "REST API to list products with category filter, sorting, and discount policy. "
                        + "Built with Java + Spring Boot 3.5.6"));
  }
}
