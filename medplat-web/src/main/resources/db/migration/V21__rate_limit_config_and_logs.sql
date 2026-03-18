-- Table to store rate limit configurations
CREATE TABLE IF NOT EXISTS rate_limit_config (
    id SERIAL PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

-- Default values
INSERT INTO rate_limit_config (config_key, config_value, description)
VALUES 
    ('RATE_LIMIT_GLOBAL', '100', 'Max requests per minute per IP for all endpoints'),
    ('RATE_LIMIT_SENSITIVE', '10', 'Max requests per minute per IP for sensitive endpoints')
ON CONFLICT (config_key) DO NOTHING;

-- Sensitive endpoints stored in DB
CREATE TABLE IF NOT EXISTS rate_limit_sensitive_endpoints (
    id SERIAL PRIMARY KEY,
    endpoint_pattern VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO rate_limit_sensitive_endpoints (endpoint_pattern)
VALUES ('/login'), ('/users'), ('/oauth'), ('/password')
ON CONFLICT (endpoint_pattern) DO NOTHING;

-- Table to log rate limit violations
CREATE TABLE IF NOT EXISTS rate_limit_violation_log (
    id SERIAL PRIMARY KEY,
    ip_address VARCHAR(100),
    endpoint VARCHAR(255),
    is_sensitive BOOLEAN,
    violated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
