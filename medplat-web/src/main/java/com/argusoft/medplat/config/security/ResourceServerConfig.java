package com.argusoft.medplat.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Satyajit
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                //                .antMatchers("/rest/**").permitAll()
                .antMatchers("/medplat-ui/bower.json").authenticated()
                .antMatchers("/medplat-ui/npm-debug.log").authenticated()
                .antMatchers("/medplat-ui/package-lock.json").authenticated()
                .antMatchers("/medplat-ui/package.json").authenticated()
                .antMatchers("/medplat-ui/sonar*").authenticated()
                .antMatchers("/medplat-ui/constants.js").permitAll()
                .antMatchers("/medplat-ui/*.js").denyAll()
                .antMatchers("/medplat-ui/docker-compose.yml").authenticated()
                .antMatchers("/medplat-ui/entrypoint.sh").authenticated()
                .antMatchers("/medplat-ui/package.json").authenticated()
                .antMatchers("/medplat-ui/**").permitAll()
                .antMatchers("/api/mobile/**").permitAll()
                .antMatchers("/api/login/get-key-and-iv").permitAll()
                .antMatchers("/api/chardham/**").permitAll()
                .antMatchers("/api/member/**").permitAll()
                .antMatchers("/api/sheetload/**").permitAll()
                .antMatchers("/api/insert_user_analytics_details/**").permitAll()
                .antMatchers("/api/user/forgotpassword/**").permitAll()
                .antMatchers("/api/systemconfigsync/**").permitAll()
                .antMatchers("/api/digilocker/**").permitAll()
                .antMatchers("/api/ndhm/healthid/create/generateotp/**").permitAll()
                .antMatchers("/api/ndhm/healthid/create/get-health-id-card-by-ndhm-user-id/**").permitAll()
                .antMatchers("/api/ndhm/healthid/create/getfile/**").permitAll()
                .antMatchers("/api/cmdashboard/**").access("#oauth2.hasScope('cm-dashboard')")
                .antMatchers("/api/**").access("#oauth2.hasScope('write')");
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ImtechoSecurityUser applicationUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        OAuth2Authentication authentication = (OAuth2Authentication) securityContext.getAuthentication();
        AuthenticationUser principal = (AuthenticationUser) authentication.getPrincipal();
        return principal.getUser();
    }
}
