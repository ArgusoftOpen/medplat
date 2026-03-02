package com.argusoft.medplat.ratelimit;

/**
 * Storage abstraction for rate limiting counters.
 * <p>
 * Allows switching between in-memory and shared stores (e.g. Redis)
 * without changing filter logic.
 */
public interface RateLimitStore {

    /**
     * Consume a single request for the given key in the provided window.
     *
     * @param key        Unique key representing a client (e.g. user or IP, bucketed by purpose)
     * @param windowMs   Window size in milliseconds
     * @param maxRequests Maximum number of allowed requests in the window
     * @return {@link RateLimitDecision} describing whether the request is allowed
     */
    RateLimitDecision consume(String key, long windowMs, int maxRequests);
}

