-- ============================
-- SURVEY CONFIGS
-- ============================
INSERT INTO system_config (config_group, config_key, config_value, value_type, category, description, is_active, is_editable, updated_at) VALUES
('SURVEY', 'survey.enabled', 'true', 'boolean', 'survey', 'Enable or disable survey feature', 1, 1, NOW()),
('SURVEY', 'survey.result.threshold.low', '40', 'integer', 'survey', 'Threshold for low risk score', 1, 1, NOW()),
('SURVEY', 'survey.result.threshold.medium', '70', 'integer', 'survey', 'Threshold for medium risk score', 1, 1, NOW()),
('SURVEY', 'survey.result.threshold.high', '90', 'integer', 'survey', 'Threshold for high risk score', 1, 1, NOW()),
('SURVEY', 'survey.auto_assign_after_threshold', 'true', 'boolean', 'survey', 'Auto-assign counselor if score exceeds threshold', 1, 1, NOW());

-- ============================
-- APPOINTMENT CONFIGS
-- ============================
INSERT INTO system_config (config_group, config_key, config_value, value_type, category, description, is_active, is_editable, updated_at) VALUES
('APPOINTMENT', 'appointment.enabled', 'true', 'boolean', 'appointment', 'Enable or disable appointment feature', 1, 1, NOW()),
('APPOINTMENT', 'appointment.max_per_day', '5', 'integer', 'appointment', 'Maximum appointments per counselor per day', 1, 1, NOW()),
('APPOINTMENT', 'appointment.allow_reschedule', 'true', 'boolean', 'appointment', 'Allow students to reschedule appointments', 1, 1, NOW()),
('APPOINTMENT', 'appointment.cancel_deadline_hours', '24', 'integer', 'appointment', 'Hours before appointment to allow cancellation', 1, 1, NOW()),
('APPOINTMENT', 'appointment.min_interval_minutes', '30', 'integer', 'appointment', 'Minimum minutes between counselor appointments', 1, 1, NOW());

-- ============================
-- SUPPORT PROGRAM CONFIGS
-- ============================
INSERT INTO system_config (config_group, config_key, config_value, value_type, category, description, is_active, is_editable, updated_at) VALUES
('SUPPORT_PROGRAM', 'support_program.enabled', 'true', 'boolean', 'support_program', 'Enable or disable support programs', 1, 1, NOW()),
('SUPPORT_PROGRAM', 'support_program.max_students_per_program', '50', 'integer', 'support_program', 'Maximum students allowed per support program', 1, 1, NOW()),
('SUPPORT_PROGRAM', 'support_program.feedback_required', 'true', 'boolean', 'support_program', 'Require feedback after program ends', 1, 1, NOW()),
('SUPPORT_PROGRAM', 'support_program.default_duration_weeks', '8', 'integer', 'support_program', 'Default number of weeks for a support program', 1, 1, NOW()),
('SUPPORT_PROGRAM', 'support_program.allow_student_leave', 'true', 'boolean', 'support_program', 'Allow students to leave a support program early', 1, 1, NOW());
