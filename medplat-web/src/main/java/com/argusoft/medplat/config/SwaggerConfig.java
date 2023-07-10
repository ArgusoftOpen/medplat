package com.argusoft.medplat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public Docket actuatorApi() {
        new ParameterBuilder()
                .parameterType("header")
                .name("Authorization")
                .modelRef(new ModelRef("string"))
                .defaultValue("bar {access_token}")
                .build();

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/cmdashboard/*"))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(oauth()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    @Bean
    SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("OAuth2")
                .scopes(scopes())
                .grantTypes(grantTypes())
                .build();
    }
    @Bean
    java.util.List<GrantType> grantTypes() {
        return Arrays.asList(new ResourceOwnerPasswordCredentialsGrant("/oauth/token"));
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId("")
                .clientSecret("")
                .realm("realm")
                .scopeSeparator(" ")
                .build();
    }

    private List<AuthorizationScope> scopes() {
        List<AuthorizationScope> list = new ArrayList<>();
        list.add(new AuthorizationScope("write", "for write operations"));
        list.add(new AuthorizationScope("cm-dashboard", "Access CM Dashboard API"));
        return list;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/cmdashboard/*"))
                .build();
    }
    java.util.List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[2];
        authorizationScopes[1] = new AuthorizationScope("write", "read and write");
        java.util.List<SecurityReference> list = new java.util.ArrayList<>();
        SecurityReference s = new SecurityReference("Authorization", authorizationScopes);
        list.add(s);
        return list;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "TeCHO+ API",
                "Application programming Interface [ API ]  is at the core of enabling interoperability. It’s a connective tissue that facilitates data sharing and enables digital health experiences. It helps to expose the actions that the user needs.\n\n" +
                        "TeCHO+ exposes the following APIs. It’s vital to know which information these APIs return.\n\n" +
                        "Detailed information on these APIs is as follows",
                null,
                null,
                new Contact("TeCHO+", "https://techo.gujarat.gov.in/medplat-ui/#!/", "techo@gujarat.gov.in"),
                null, null, Collections.emptyList());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/META-INF/resources/");
    }
    @Override public void addViewControllers(ViewControllerRegistry registry) { registry.addRedirectViewController("/docs/v2/api-docs", "/v2/api-docs").setKeepQueryParams(true); registry.addRedirectViewController("/docs/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui"); registry.addRedirectViewController("/docs/swagger-resources/configuration/security", "/swagger-resources/configuration/security"); registry.addRedirectViewController("/docs/swagger-resources", "/swagger-resources"); }

}