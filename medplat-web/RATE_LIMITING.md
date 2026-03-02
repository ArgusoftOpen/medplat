## Overview

This backend exposes a middleware-based, configurable API rate limiting layer to protect public and sensitive endpoints from abuse.

Rate limiting is enforced centrally using a servlet filter that:

- Tracks requests per client over a rolling time window
- Applies a **global** limit for all endpoints
- Applies **stricter** limits for sensitive endpoints such as authentication and admin routes
- Keys clients by **user ID** when authenticated, or by **IP address** otherwise
- Logs all violations with IP, endpoint, and timestamp

The implementation is designed to be easily replaceable with a shared store (e.g. Redis) without changing controller or security code.

---

## How It Works

### Core Components

- `com.argusoft.medplat.ratelimit.RateLimitingFilter`
  - `OncePerRequestFilter` that runs early in the filter chain
  - Enforces global and sensitive-route limits
  - Emits HTTP `429 Too Many Requests` with a stable JSON body on violation

- `com.argusoft.medplat.ratelimit.RateLimitConfig`
  - Centralizes rate limit configuration
  - Reads environment variables and provides sane defaults
  - Maintains the list of sensitive endpoint patterns

- `com.argusoft.medplat.ratelimit.RateLimitStore`
  - Abstraction for the underlying counter storage
  - Allows swapping from in-memory to Redis or another distributed store

- `com.argusoft.medplat.ratelimit.InMemoryRateLimitStore`
  - Default `RateLimitStore` implementation
  - Uses a fixed window per key with a thread-safe `ConcurrentHashMap`

### Identity Strategy

Each request is mapped to a logical **identity key**:

- If the request is authenticated:
  - Uses the authenticated user ID from `AuthenticationUser` / `ImtechoSecurityUser`
  - Key format: `user:{userId}`
- If the request is anonymous:
  - Uses the client IP (preferring `X-Forwarded-For`, then `X-Real-IP`, then `remoteAddr`)
  - Key format: `ip:{ipAddress}`

This key is then partitioned into different buckets:

- Global bucket: `global:{identityKey}`
- Sensitive bucket: `sensitive:{identityKey}`

Each bucket is tracked independently, so a client can hit the global limit, the sensitive limit, or both.

### Request Flow

For every incoming HTTP request:

1. **Exclusions**
   - The filter skips a few infrastructural/static endpoints (e.g. `/actuator/health`, heavy static content) where rate limiting is not useful.

2. **Global Limit**
   - The filter calls `RateLimitStore.consume("global:{identityKey}", windowMs, globalMaxRequests)`.
   - If the request exceeds the configured global limit:
     - The filter logs a `GLOBAL` violation.
     - The filter returns `429 Too Many Requests` and **does not** call downstream filters/controllers.

3. **Sensitive Endpoint Limit**
   - If the request path matches one of the configured sensitive patterns, the filter also calls:
     - `RateLimitStore.consume("sensitive:{identityKey}", windowMs, authMaxRequests)`.
   - If this stricter limit is exceeded:
     - The filter logs a `SENSITIVE` violation.
     - The filter returns `429 Too Many Requests`.

4. **Pass-through**
   - If all applicable checks pass, the filter delegates to the rest of the filter chain as normal.

---

## Configuration Variables

Rate limiting is configured primarily via environment variables, making it easy to tune per environment without code changes.

### Required / Supported Variables

- `RATE_LIMIT_WINDOW_MS`
  - **Description**: Time window size in milliseconds.
  - **Default**: `900000` (15 minutes).
  - **Example**: `RATE_LIMIT_WINDOW_MS=60000` for 1-minute windows.

- `RATE_LIMIT_MAX`
  - **Description**: Global maximum number of requests allowed per identity per window.
  - **Default**: `100`.
  - **Example**: `RATE_LIMIT_MAX=500` for a more relaxed global limit.

- `RATE_LIMIT_AUTH_MAX`
  - **Description**: Maximum number of requests allowed per identity per window for **sensitive endpoints**.
  - **Default**: `5`.
  - **Example**: `RATE_LIMIT_AUTH_MAX=10` for slightly less strict sensitive limits.

- `RATE_LIMIT_SENSITIVE_ENDPOINTS` (optional)
  - **Description**: Comma-separated list of Ant-style path patterns treated as “sensitive”.
  - **Default**:
    - `/auth/login`
    - `/auth/register`
    - `/user/**`
    - `/admin/**`
    - `/api/login/**`
    - `/api/user/**`
    - `/api/admin/**`
  - **Example**:
    - `RATE_LIMIT_SENSITIVE_ENDPOINTS=/auth/login,/auth/register,/api/auth/**,/api/admin/**`

> Note: If `RATE_LIMIT_SENSITIVE_ENDPOINTS` is not provided, the default list above is used.

---

## Default Limits

Out of the box, without any environment overrides:

- **Time window**: 15 minutes (`900000 ms`)
- **Global limit**: 100 requests per 15 minutes per identity (IP or user)
- **Sensitive limit**: 5 requests per 15 minutes per identity on sensitive endpoints

This means, for example:

- An anonymous client is allowed **100 total** requests every 15 minutes based on IP.
- The same client can only hit login or other sensitive endpoints **5 times** every 15 minutes.
- An authenticated user is similarly limited by user ID.

---

## Error Response

When a client exceeds either the global or sensitive limit, the filter returns:

- **HTTP Status**: `429 Too Many Requests`
- **Body**:

```json
{
  "success": false,
  "message": "Too many requests, please try again later"
}
```

The content type is `application/json`, and no further processing (controllers, other filters) is performed for that request.

---

## Logging of Violations

Every rate limit violation is logged using the standard application logger with:

- Bucket type: `GLOBAL` or `SENSITIVE`
- Client IP
- User key (ID or principal name when available)
- Request path
- Remaining quota at the time of violation
- Window reset timestamp

This allows operations and security teams to monitor abuse patterns and tune limits.

Example log (simplified):

```text
WARN  Rate limit exceeded - bucket=SENSITIVE, ip=203.0.113.5, userKey=1234, path=/auth/login, remaining=0, resetAt=2026-03-01T12:34:56Z
```

---

## How to Change Limits

1. Decide the new limits for your environment (e.g. staging vs production).
2. Set or update the following environment variables before starting the backend:
   - `RATE_LIMIT_WINDOW_MS`
   - `RATE_LIMIT_MAX`
   - `RATE_LIMIT_AUTH_MAX`
3. Restart the backend service so the new configuration is picked up.

No code changes or redeploys are required beyond the normal restart with updated environment.

---

## How to Add New Protected Routes

To add additional sensitive endpoints (e.g. new admin APIs or payment-related routes):

1. Identify the URL pattern, e.g. `/api/payments/**`.
2. Append it to the `RATE_LIMIT_SENSITIVE_ENDPOINTS` environment variable:
   - Example:
     - `RATE_LIMIT_SENSITIVE_ENDPOINTS=/auth/login,/auth/register,/api/auth/**,/api/admin/**,/api/payments/**`
3. Restart the backend to apply the updated configuration.

Alternatively, if you are fine using the defaults, ensure that your new endpoints fit within the existing patterns (e.g. place new admin routes under `/api/admin/**`).

---

## Storage and Scalability

### Current Implementation

- Uses `InMemoryRateLimitStore` backed by a `ConcurrentHashMap`.
- Suitable for:
  - Single-instance deployments.
  - Small clusters where strict global consistency is not required.

### Moving to Redis or Another Shared Store

The system is designed so that switching to a distributed store requires only:

1. Implementing `RateLimitStore` with a Redis-backed implementation (e.g. using atomic LUA scripts or `INCR`/`EXPIRE` semantics).
2. Registering that implementation as the primary `RateLimitStore` bean (e.g. via `@Primary`).

No changes are needed in controllers, security configuration, or the `RateLimitingFilter`.

---

## Validation Notes

- The rate limiting filter runs in addition to existing CORS, no-cache, request logging and Spring Security filters.
- If the application fails to start due to database configuration (e.g. Postgres credentials), rate limiting does not interfere; it is only active once the servlet context is fully initialized.
- To locally validate behavior:
  - Configure a reachable database (as per `jdbc.properties` / profile settings).
  - Start the backend.
  - Send repeated requests to any endpoint and confirm that:
    - After 100 requests in the configured window, further ones receive `429`.
    - Sensitive endpoints begin returning `429` after the stricter limit (default 5) is exceeded.

