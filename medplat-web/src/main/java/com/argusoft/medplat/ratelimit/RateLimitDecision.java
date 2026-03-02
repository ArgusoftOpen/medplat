package com.argusoft.medplat.ratelimit;

/**
 * Result of a single rate limit check/consume operation.
 */
public class RateLimitDecision {

    private final boolean allowed;
    private final int remaining;
    private final long resetAtMillis;

    public RateLimitDecision(boolean allowed, int remaining, long resetAtMillis) {
        this.allowed = allowed;
        this.remaining = remaining;
        this.resetAtMillis = resetAtMillis;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public int getRemaining() {
        return remaining;
    }

    public long getResetAtMillis() {
        return resetAtMillis;
    }
}

