package com.argusoft.medplat.config;

import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements Filter {

    private static final Logger logger =
            LoggerFactory.getLogger(RateLimitingFilter.class);

    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    @Value("${rate.limit.global.capacity:100}")
    private int globalCapacity;

    @Value("${rate.limit.global.window:15}")
    private long globalWindow;

    @Value("${rate.limit.auth.capacity:5}")
    private int authCapacity;

    @Value("${rate.limit.auth.window:15}")
    private long authWindow;

    private Bucket createBucket(int capacity, long minutes) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(capacity)
                .refillIntervally(capacity, Duration.ofMinutes(minutes))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String clientKey = getClientKey(req);
        String path = req.getRequestURI();

        boolean isSensitive =
                path.contains("/login") ||
                path.contains("/forgot-password") ||
                path.contains("/user");

        int capacity = isSensitive ? authCapacity : globalCapacity;
        long window = isSensitive ? authWindow : globalWindow;

        Bucket bucket = bucketCache.computeIfAbsent(
                clientKey,
                key -> createBucket(capacity, window)
        );

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {

            logger.warn("Rate limit exceeded for key: {} on path: {}",
                    clientKey, path);

            res.setStatus(429);
            res.setContentType("application/json");
            res.getWriter().write(
                    "{\"error\":\"Too many requests. Please try again later.\"}"
            );
        }
    }

    private String getClientKey(HttpServletRequest request) {
        String user = request.getUserPrincipal() != null
                ? request.getUserPrincipal().getName()
                : null;

        if (user != null) {
            return "USER_" + user;
        }

        return "IP_" + request.getRemoteAddr();
    }
}