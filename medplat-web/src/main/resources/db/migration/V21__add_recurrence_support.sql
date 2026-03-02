ALTER TABLE event_configuration
ADD COLUMN recurrence_type VARCHAR(50);

ALTER TABLE event_configuration
ADD COLUMN recurrence_end_date TIMESTAMP;

ALTER TABLE event_configuration
ADD COLUMN recurrence_count INTEGER;

ALTER TABLE event_configuration
ADD COLUMN week_day INTEGER;