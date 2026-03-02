CREATE TABLE dashboard_configuration (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    dashboard_name VARCHAR(255) NOT NULL,
    description TEXT,
    dashboard_type VARCHAR(50),
    configuration JSONB,
    is_default BOOLEAN DEFAULT FALSE,
    is_public BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    role_based_access VARCHAR(500),
    refresh_interval_seconds INTEGER DEFAULT 300,
    ai_enabled BOOLEAN DEFAULT TRUE,
    nlp_enabled BOOLEAN DEFAULT TRUE,
    anomaly_detection_enabled BOOLEAN DEFAULT FALSE,
    predictive_analytics_enabled BOOLEAN DEFAULT FALSE,
    recommendation_enabled BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    version BIGINT DEFAULT 1,
    CONSTRAINT dashboard_config_user_id_fk FOREIGN KEY (user_id) REFERENCES master_user(id)
);
CREATE INDEX idx_dashboard_config_user_id ON dashboard_configuration(user_id);
CREATE INDEX idx_dashboard_config_is_active ON dashboard_configuration(is_active);
CREATE INDEX idx_dashboard_config_dashboard_type ON dashboard_configuration(dashboard_type);
CREATE INDEX idx_dashboard_config_ai_enabled ON dashboard_configuration(ai_enabled);
CREATE TABLE dashboard_widget (
    id BIGSERIAL PRIMARY KEY,
    dashboard_id BIGINT NOT NULL,
    widget_name VARCHAR(255) NOT NULL,
    widget_type VARCHAR(50) NOT NULL,
    position INTEGER,
    grid_row INTEGER,
    grid_column INTEGER,
    grid_width INTEGER DEFAULT 4,
    grid_height INTEGER DEFAULT 200,
    title VARCHAR(255),
    description TEXT,
    data_source_id BIGINT,
    query_configuration JSONB,
    visualization_config JSONB,
    display_options JSONB,
    aggregation_type VARCHAR(50),
    time_series_enabled BOOLEAN DEFAULT FALSE,
    time_granularity VARCHAR(50),
    cache_duration_seconds INTEGER DEFAULT 300,
    refresh_interval_seconds INTEGER DEFAULT 300,
    ai_anomaly_detection BOOLEAN DEFAULT FALSE,
    ai_forecast BOOLEAN DEFAULT FALSE,
    ai_insights BOOLEAN DEFAULT TRUE,
    ai_config JSONB,
    is_active BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT widget_dashboard_id_fk FOREIGN KEY (dashboard_id) REFERENCES dashboard_configuration(id) ON DELETE CASCADE
);
CREATE INDEX idx_dashboard_widget_dashboard_id ON dashboard_widget(dashboard_id);
CREATE INDEX idx_dashboard_widget_data_source_id ON dashboard_widget(data_source_id);
CREATE INDEX idx_dashboard_widget_widget_type ON dashboard_widget(widget_type);
CREATE INDEX idx_dashboard_widget_is_active ON dashboard_widget(is_active);
CREATE TABLE dashboard_data_source (
    id BIGSERIAL PRIMARY KEY,
    source_name VARCHAR(255) NOT NULL UNIQUE,
    source_type VARCHAR(50) NOT NULL,
    description TEXT,
    connection_config JSONB,
    query TEXT,
    schema_mapping JSONB,
    caching_strategy VARCHAR(50),
    cache_ttl_seconds INTEGER DEFAULT 3600,
    retry_attempts INTEGER DEFAULT 3,
    timeout_seconds INTEGER DEFAULT 30,
    pagination_enabled BOOLEAN DEFAULT FALSE,
    page_size INTEGER DEFAULT 1000,
    validation_rules JSONB,
    data_transformation JSONB,
    real_time_enabled BOOLEAN DEFAULT FALSE,
    streaming_config JSONB,
    required_role VARCHAR(50),
    is_public BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    sample_data JSONB,
    total_records BIGINT,
    last_synced TIMESTAMP,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    CONSTRAINT data_source_source_name_unique UNIQUE(source_name)
);
CREATE INDEX idx_data_source_source_type ON dashboard_data_source(source_type);
CREATE INDEX idx_data_source_is_active ON dashboard_data_source(is_active);
CREATE INDEX idx_data_source_created_by ON dashboard_data_source(created_by);
CREATE TABLE dashboard_ai_insight (
    id BIGSERIAL PRIMARY KEY,
    widget_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    insight_type VARCHAR(50) NOT NULL,
    severity VARCHAR(50),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    insight_data JSONB,
    confidence_score NUMERIC(3,2),
    relevant_metrics JSONB,
    recommended_action TEXT,
    reference_link VARCHAR(500),
    ai_model_used VARCHAR(255),
    is_acknowledged BOOLEAN DEFAULT FALSE,
    acknowledged_date TIMESTAMP,
    is_resolved BOOLEAN DEFAULT FALSE,
    resolved_date TIMESTAMP,
    user_feedback VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_date TIMESTAMP,
    CONSTRAINT insight_widget_id_fk FOREIGN KEY (widget_id) REFERENCES dashboard_widget(id) ON DELETE CASCADE,
    CONSTRAINT insight_user_id_fk FOREIGN KEY (user_id) REFERENCES master_user(id)
);
CREATE INDEX idx_ai_insight_widget_id ON dashboard_ai_insight(widget_id);
CREATE INDEX idx_ai_insight_user_id ON dashboard_ai_insight(user_id);
CREATE INDEX idx_ai_insight_insight_type ON dashboard_ai_insight(insight_type);
CREATE INDEX idx_ai_insight_severity ON dashboard_ai_insight(severity);
CREATE INDEX idx_ai_insight_is_acknowledged ON dashboard_ai_insight(is_acknowledged);
CREATE INDEX idx_ai_insight_created_date ON dashboard_ai_insight(created_date);
CREATE TABLE dashboard_user_preference (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    default_dashboard_id BIGINT,
    theme VARCHAR(50),
    language VARCHAR(10),
    auto_refresh_enabled BOOLEAN DEFAULT TRUE,
    refresh_interval_seconds INTEGER DEFAULT 300,
    ai_insights_enabled BOOLEAN DEFAULT TRUE,
    anomaly_alerts_enabled BOOLEAN DEFAULT TRUE,
    email_alerts_enabled BOOLEAN DEFAULT FALSE,
    preferences JSONB,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_pref_user_id_fk FOREIGN KEY (user_id) REFERENCES master_user(id),
    CONSTRAINT user_pref_dashboard_id_fk FOREIGN KEY (default_dashboard_id) REFERENCES dashboard_configuration(id)
);
CREATE INDEX idx_user_preference_user_id ON dashboard_user_preference(user_id);
CREATE TABLE dashboard_user_activity (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    dashboard_id BIGINT,
    widget_id BIGINT,
    action_type VARCHAR(50),
    action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metadata JSONB,
    CONSTRAINT activity_user_id_fk FOREIGN KEY (user_id) REFERENCES master_user(id),
    CONSTRAINT activity_dashboard_id_fk FOREIGN KEY (dashboard_id) REFERENCES dashboard_configuration(id) ON DELETE SET NULL,
    CONSTRAINT activity_widget_id_fk FOREIGN KEY (widget_id) REFERENCES dashboard_widget(id) ON DELETE SET NULL
);
CREATE INDEX idx_user_activity_user_id ON dashboard_user_activity(user_id);
CREATE INDEX idx_user_activity_action_timestamp ON dashboard_user_activity(action_timestamp);
CREATE INDEX idx_user_activity_action_type ON dashboard_user_activity(action_type);
CREATE TABLE dashboard_filter (
    id BIGSERIAL PRIMARY KEY,
    dashboard_id BIGINT NOT NULL,
    filter_name VARCHAR(255) NOT NULL,
    filter_type VARCHAR(50),
    data_field VARCHAR(255),
    default_value VARCHAR(500),
    allowed_values JSONB,
    is_required BOOLEAN DEFAULT FALSE,
    is_visible BOOLEAN DEFAULT TRUE,
    display_order INTEGER,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT filter_dashboard_id_fk FOREIGN KEY (dashboard_id) REFERENCES dashboard_configuration(id) ON DELETE CASCADE
);
CREATE INDEX idx_dashboard_filter_dashboard_id ON dashboard_filter(dashboard_id);
CREATE TABLE dashboard_nlp_query_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    query_text TEXT NOT NULL,
    intent VARCHAR(50),
    confidence_score NUMERIC(3,2),
    response_data JSONB,
    is_useful BOOLEAN,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT nlp_history_user_id_fk FOREIGN KEY (user_id) REFERENCES master_user(id)
);
CREATE INDEX idx_nlp_history_user_id ON dashboard_nlp_query_history(user_id);
CREATE INDEX idx_nlp_history_created_date ON dashboard_nlp_query_history(created_date);
CREATE TABLE dashboard_export_import_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    dashboard_id BIGINT,
    action_type VARCHAR(20),
    file_name VARCHAR(255),
    export_data JSONB,
    description TEXT,
    status VARCHAR(50),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT export_log_user_id_fk FOREIGN KEY (user_id) REFERENCES master_user(id),
    CONSTRAINT export_log_dashboard_id_fk FOREIGN KEY (dashboard_id) REFERENCES dashboard_configuration(id)
);
CREATE INDEX idx_export_log_user_id ON dashboard_export_import_log(user_id);
CREATE INDEX idx_export_log_action_type ON dashboard_export_import_log(action_type);
CREATE SEQUENCE dashboard_configuration_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE dashboard_widget_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE dashboard_data_source_seq START WITH 1 INCREMENT BY 1;
COMMIT;
