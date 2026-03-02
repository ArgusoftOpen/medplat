package com.argusoft.medplat.ratelimit;

import com.argusoft.medplat.config.security.AuthenticationUser;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * Global API rate limiting filter.
 * <p>
 * Responsibilities:
 * - Enforce global rate limit per client (IP or authenticated user)
 * - Enforce stricter limits for sensitive endpoints
 * - Log violations with IP, endpoint and timestamp
 * - Return standardized 429 JSON response when limits are exceeded
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    private static final String RATE_LIMIT_RESPONSE_BODY =
            "{\"success\":false,\"message\":\"Too many requests, please try again later\"}";

    private final RateLimitConfig rateLimitConfig;
    private final RateLimitStore rateLimitStore;

    public RateLimitingFilter(RateLimitConfig rateLimitConfig, RateLimitStore rateLimitStore) {
        this.rateLimitConfig = rateLimitConfig;
        this.rateLimitStore = rateLimitStore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allow some infrastructure and static endpoints to bypass rate limiting if needed.
        if (isExcludedPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = extractClientIp(request);
        String userKey = resolveUserKey();
        String identityKey = (userKey != null) ? "user:" + userKey : "ip:" + clientIp;

        long windowMs = rateLimitConfig.getWindowMs();

        // Global bucket
        RateLimitDecision globalDecision = rateLimitStore.consume("global:" + identityKey,
                windowMs, rateLimitConfig.getGlobalMaxRequests());
        if (!globalDecision.isAllowed()) {
            logViolation("GLOBAL", path, clientIp, userKey, globalDecision);
            writeTooManyRequests(response);
            return;
        }

        // Sensitive endpoints bucket (stricter)
        if (rateLimitConfig.isSensitivePath(path)) {
            RateLimitDecision sensitiveDecision = rateLimitStore.consume("sensitive:" + identityKey,
                    windowMs, rateLimitConfig.getAuthMaxRequests());
            if (!sensitiveDecision.isAllowed()) {
                logViolation("SENSITIVE", path, clientIp, userKey, sensitiveDecision);
                writeTooManyRequests(response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExcludedPath(String path) {
        if (path == null) {
            return true;
        }
        // Avoid rate limiting for actuator health and basic static resources.
        return path.startsWith("/actuator/health")
                || path.startsWith("/medplat-ui/third_party/")
                || path.startsWith("/medplat-ui/assets/")
                || path.startsWith("/medplat-ui/fonts/")
                || path.startsWith("/medplat-ui/images/");
    }

    private String extractClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xff)) {
            int idx = xff.indexOf(',');
            return (idx >= 0) ? xff.substring(0, idx).trim() : xff.trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

    private String resolveUserKey() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                return null;
            }
            Object principal = authentication.getPrincipal();
            if (principal instanceof AuthenticationUser) {
                ImtechoSecurityUser user = ((AuthenticationUser) principal).getUser();
                if (user != null && user.getId() != null) {
                    return String.valueOf(user.getId());
                }
            }
            // Fallback to authentication name
            if (StringUtils.hasText(authentication.getName())) {
                return authentication.getName();
            }
        } catch (Exception ex) {
            logger.debug("Failed to resolve user for rate limiting: {}", ex.getMessage());
        }
        return null;
    }

    private void logViolation(String bucket,
                              String path,
                              String clientIp,
                              String userKey,
                              RateLimitDecision decision) {
        logger.warn(
                "Rate limit exceeded - bucket={}, ip={}, userKey={}, path={}, remaining={}, resetAt={}",
                bucket,
                clientIp,
                userKey,
                path,
                decision.getRemaining(),
                Instant.ofEpochMilli(decision.getResetAtMillis())
        );
    }

    private void writeTooManyRequests(HttpServletResponse response) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        response.setStatus(429);
        response.setContentType("application/json");
        byte[] body = RATE_LIMIT_RESPONSE_BODY.getBytes(StandardCharsets.UTF_8);
        response.setContentLength(body.length);
        response.getOutputStream().write(body);
    }
}

