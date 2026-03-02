package com.argusoft.medplat.ratelimit;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Default in-memory implementation of {@link RateLimitStore}.
 * <p>
 * Uses a simple fixed-window counter per key. This keeps the implementation
 * straightforward while still being suitable for many production workloads.
 */
@Component
public class InMemoryRateLimitStore implements RateLimitStore {

    private static final class Counter {
        final long windowStartMillis;
        final int count;

        Counter(long windowStartMillis, int count) {
            this.windowStartMillis = windowStartMillis;
            this.count = count;
        }
    }

    private final ConcurrentHashMap<String, Counter> counters = new ConcurrentHashMap<>();

    @Override
    public RateLimitDecision consume(String key, long windowMs, int maxRequests) {
        long now = System.currentTimeMillis();
        AtomicReference<Boolean> allowedRef = new AtomicReference<>(false);
        AtomicReference<Counter> updatedCounterRef = new AtomicReference<>();

        counters.compute(key, (k, existing) -> {
            Counter current = existing;

            // Start a new window if there is no counter or the window has expired
            if (current == null || now - current.windowStartMillis >= windowMs) {
                Counter fresh = new Counter(now, 1);
                allowedRef.set(true);
                updatedCounterRef.set(fresh);
                return fresh;
            }

            // Still within the current window
            if (current.count < maxRequests) {
                Counter incremented = new Counter(current.windowStartMillis, current.count + 1);
                allowedRef.set(true);
                updatedCounterRef.set(incremented);
                return incremented;
            }

            // Over limit for this window
            allowedRef.set(false);
            updatedCounterRef.set(current);
            return current;
        });

        Counter finalCounter = updatedCounterRef.get();
        if (finalCounter == null) {
            // Should not happen, but be defensive
            return new RateLimitDecision(true, maxRequests - 1, now + windowMs);
        }

        boolean allowed = allowedRef.get();
        int remaining = allowed ? Math.max(0, maxRequests - finalCounter.count) : 0;
        long resetAt = finalCounter.windowStartMillis + windowMs;

        // Optional: basic cleanup to avoid unbounded memory growth
        if (now - finalCounter.windowStartMillis >= (2L * windowMs)) {
            counters.remove(key, finalCounter);
        }

        return new RateLimitDecision(allowed, remaining, resetAt);
    }
}

