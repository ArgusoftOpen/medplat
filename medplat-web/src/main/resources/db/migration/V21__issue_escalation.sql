-- Minimal: add escalation flag to existing techo_web_notification_master (issue/task entity)
ALTER TABLE public.techo_web_notification_master ADD COLUMN IF NOT EXISTS is_escalated boolean DEFAULT false;
