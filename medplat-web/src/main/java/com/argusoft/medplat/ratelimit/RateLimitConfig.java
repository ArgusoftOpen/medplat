package com.argusoft.medplat.ratelimit;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Central configuration for API rate limiting.
 * Values are primarily driven by environment variables:
 * RATE_LIMIT_WINDOW_MS, RATE_LIMIT_MAX, RATE_LIMIT_AUTH_MAX, RATE_LIMIT_SENSITIVE_ENDPOINTS.
 */
@Component
public class RateLimitConfig {

    private static final long DEFAULT_WINDOW_MS = 900_000L; // 15 minutes
    private static final int DEFAULT_GLOBAL_MAX = 100;
    private static final int DEFAULT_AUTH_MAX = 5;
    private static final String DEFAULT_SENSITIVE_ENDPOINTS =
            "/auth/login," +
                    "/auth/register," +
                    "/user/**," +
                    "/admin/**," +
                    "/api/login/**," +
                    "/api/user/**," +
                    "/api/admin/**";

    private final long windowMs;
    private final int globalMaxRequests;
    private final int authMaxRequests;
    private final List<String> sensitiveEndpointPatterns;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public RateLimitConfig() {
        Map<String, String> env = System.getenv();

        this.windowMs = parseLong(env.get("RATE_LIMIT_WINDOW_MS"), DEFAULT_WINDOW_MS);
        this.globalMaxRequests = parseInt(env.get("RATE_LIMIT_MAX"), DEFAULT_GLOBAL_MAX);
        this.authMaxRequests = parseInt(env.get("RATE_LIMIT_AUTH_MAX"), DEFAULT_AUTH_MAX);

        String rawSensitive = env.getOrDefault("RATE_LIMIT_SENSITIVE_ENDPOINTS", DEFAULT_SENSITIVE_ENDPOINTS);
        this.sensitiveEndpointPatterns = Arrays.stream(rawSensitive.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public long getWindowMs() {
        return windowMs;
    }

    public int getGlobalMaxRequests() {
        return globalMaxRequests;
    }

    public int getAuthMaxRequests() {
        return authMaxRequests;
    }

    public List<String> getSensitiveEndpointPatterns() {
        return sensitiveEndpointPatterns;
    }

    /**
     * Returns true if the given path matches one of the configured sensitive endpoint patterns.
     */
    public boolean isSensitivePath(String path) {
        if (path == null) {
            return false;
        }
        for (String pattern : sensitiveEndpointPatterns) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    private long parseLong(String value, long defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}

