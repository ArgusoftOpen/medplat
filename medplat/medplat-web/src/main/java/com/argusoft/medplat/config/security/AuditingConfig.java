package com.argusoft.medplat.config.security;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Optional;

/**
 *
 * @author vaishali
 */
@EnableJpaAuditing
@Configuration
public class AuditingConfig {

    @Autowired
    private MobileUser mobileUser;

    @Bean
    public AuditorAware<Integer> createAuditorProvider() {
        return new SecurityAuditor();
    }

    @Bean
    public AuditingEntityListener createAuditingListener() {
        return new AuditingEntityListener();
    }

    public class SecurityAuditor implements AuditorAware<Integer> {

        @Override
        public Optional<Integer> getCurrentAuditor() {
            Integer id;
            try {
                if (mobileUser != null && mobileUser.getId() != null) {
                    id = mobileUser.getId();
                } else {
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    if (securityContext.getAuthentication() instanceof OAuth2Authentication) {
                        OAuth2Authentication authentication = (OAuth2Authentication) securityContext.getAuthentication();
                        AuthenticationUser principal = (AuthenticationUser) authentication.getPrincipal();
                        if (principal != null && principal.getUser().getId() != null) {
                            id = principal.getUser().getId();
                        } else {
                            id = -1;
                        }
                    } else {
                        id = -1;
                    }
                }
            } catch (BeanCreationException beanCreationException) {
                id = -1;
            }
            return Optional.of(id);
        }
    }

}
