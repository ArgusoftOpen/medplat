package com.argusoft.medplat.config.security;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(myAuthProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v0.5/**");
    }

    @Bean
    public CustomDaoAuthenticationProvider myAuthProvider() {
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
        provider.setPasswordEncoder(md5PasswordEncoder());
        provider.setUserDetailsService(springSecurityUserService());
        return provider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public PasswordEncoder md5PasswordEncoder() {
        return new PasswordEncoder() {
            private BasicPasswordEncryptor basicPasswordEncryptor = new BasicPasswordEncryptor();

            @Override
            public String encode(CharSequence rawPassword) {
                return basicPasswordEncryptor.encryptPassword(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return basicPasswordEncryptor.checkPassword(rawPassword.toString(), encodedPassword);
            }
        };
    }

    @Bean
    public PasswordEncoder plainPasswordEncoder() {     // it's used for oauth  client and it's secret
        return new PasswordEncoder() {


            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

    @Bean
    public SecurityUserService springSecurityUserService() {
        return new SecurityUserService();
    }

}
