package com.argusoft.medplat.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class RateLimitingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingFilter.class);

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final int globalLimit = Integer.parseInt(
            System.getenv().getOrDefault("RATE_LIMIT_GLOBAL", "100"));

    private final int sensitiveLimit = Integer.parseInt(
            System.getenv().getOrDefault("RATE_LIMIT_SENSITIVE", "10"));

    private Bucket createBucket(int limit) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(limit,
                        Refill.intervally(limit, Duration.ofMinutes(1))))
                .build();
    }

    private boolean isSensitivePath(String path) {
        return path.contains("/login") ||
               path.contains("/users") ||
               path.contains("/oauth") ||
               path.contains("/password");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();
        boolean sensitive = isSensitivePath(path);

        int limit = sensitive ? sensitiveLimit : globalLimit;
        String bucketKey = ip + ":" + (sensitive ? "sensitive" : "global");

        Bucket bucket = buckets.computeIfAbsent(bucketKey, k -> createBucket(limit));

        if (bucket.tryConsume(1)) {
            chain.doFilter(req, res);
        } else {
            log.warn("[RATE LIMIT VIOLATED] IP={} | Path={} | Sensitive={}", ip, path, sensitive);
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"status\": 429, " +
                    "\"error\": \"Too Many Requests\", " +
                    "\"message\": \"Rate limit exceeded. Please try again later.\"}"
            );
        }
    }
}
