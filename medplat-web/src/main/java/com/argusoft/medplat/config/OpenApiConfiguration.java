package com.argusoft.medplat.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DPG API Documentation")
                        .description("This Swagger UI provides a comprehensive overview of all available API endpoints used in the DPG application.")
                        .contact(contact())
                )
                .servers(List.of(
                        new Server().url("https://dpg.argusoft.com/medplat-ui/").description("Production Server"),
                        new Server().url("http://localhost:8181").description("Local Development Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth").addList("X-Client-Date-Time"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }

    private Contact contact() {
        return new Contact()
                .name("DPG")
                .url("https://dpg.argusoft.com/medplat-ui/");
    }

    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder()
                .group("@All")
                .packagesToScan("com.argusoft.medplat")
                .build(); // No filters => includes all APIs
    }


    @Bean
    public GroupedOpenApi fhsApis() {
        return GroupedOpenApi.builder()
                .group("FHS")
                .packagesToScan("com.argusoft.medplat.fhs")
                .build();
    }


    @Bean
    public GroupedOpenApi documentApis() {
        return GroupedOpenApi.builder()
                .group("Document")
                .packagesToScan("com.argusoft.medplat.document")
                .build();
    }


    @Bean
    public GroupedOpenApi codeApis() {
        return GroupedOpenApi.builder()
                .group("Code")
                .packagesToScan("com.argusoft.medplat.code")
                .build();
    }


    @Bean
    public GroupedOpenApi trainingApis() {
        return GroupedOpenApi.builder()
                .group("Training")
                .packagesToScan("com.argusoft.medplat.training")
                .build();
    }


    @Bean
    public GroupedOpenApi notificationApis() {
        return GroupedOpenApi.builder()
                .group("Notification")
                .packagesToScan("com.argusoft.medplat.notification")
                .build();
    }


    @Bean
    public GroupedOpenApi courseApis() {
        return GroupedOpenApi.builder()
                .group("Course")
                .packagesToScan("com.argusoft.medplat.course")
                .build();
    }


    @Bean
    public GroupedOpenApi queryApis() {
        return GroupedOpenApi.builder()
                .group("Query")
                .packagesToScan("com.argusoft.medplat.query")
                .build();
    }


    @Bean
    public GroupedOpenApi eventApis() {
        return GroupedOpenApi.builder()
                .group("Event")
                .packagesToScan("com.argusoft.medplat.event")
                .build();
    }


    @Bean
    public GroupedOpenApi commonApis() {
        return GroupedOpenApi.builder()
                .group("Common")
                .packagesToScan("com.argusoft.medplat.common")
                .build();
    }


    @Bean
    public GroupedOpenApi configApis() {
        return GroupedOpenApi.builder()
                .group("Config")
                .packagesToScan("com.argusoft.medplat.config")
                .build();
    }


    @Bean
    public GroupedOpenApi internationalizationApis() {
        return GroupedOpenApi.builder()
                .group("Internationalization")
                .packagesToScan("com.argusoft.medplat.internationalization")
                .build();
    }


    @Bean
    public GroupedOpenApi listvaluesApis() {
        return GroupedOpenApi.builder()
                .group("List Values")
                .packagesToScan("com.argusoft.medplat.listvalues")
                .build();
    }


    @Bean
    public GroupedOpenApi mobileApis() {
        return GroupedOpenApi.builder()
                .group("Mobile")
                .packagesToScan("com.argusoft.medplat.mobile")
                .build();
    }


    @Bean
    public GroupedOpenApi fcmApis() {
        return GroupedOpenApi.builder()
                .group("FCM")
                .packagesToScan("com.argusoft.medplat.fcm")
                .build();
    }


    @Bean
    public GroupedOpenApi reportconfigApis() {
        return GroupedOpenApi.builder()
                .group("Report Config")
                .packagesToScan("com.argusoft.medplat.reportconfig")
                .build();
    }


    @Bean
    public GroupedOpenApi rchApis() {
        return GroupedOpenApi.builder()
                .group("RCH")
                .packagesToScan("com.argusoft.medplat.rch")
                .build();
    }


    @Bean
    public GroupedOpenApi ncddnhddApis() {
        return GroupedOpenApi.builder()
                .group("NCD DNHDD")
                .packagesToScan("com.argusoft.medplat.ncddnhdd")
                .build();
    }


    @Bean
    public GroupedOpenApi systemconstraintApis() {
        return GroupedOpenApi.builder()
                .group("System Constraint")
                .packagesToScan("com.argusoft.medplat.systemconstraint")
                .build();
    }

}
