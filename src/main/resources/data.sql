DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;


CREATE TABLE QRTZ_JOB_DETAILS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(200) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(200) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME  VARCHAR(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(200) NULL,
    JOB_GROUP VARCHAR(200) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40) NOT NULL,
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

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

INSERT INTO system_config (config_group, config_key, config_value, value_type, category, description, is_active, is_editable, updated_at) VALUES
                                                                                                                                              ('FILE', 'file.size', '5', 'long', 'file_size', 'File upload size (MB)', 1, 1, NOW()),




    INSERT INTO accounts (email, password, role, status, phone_number, full_name, gender, dob, created_date)
VALUES
-- Existing records
('danhkvtse172932@fpt.edu.vn', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'MANAGER', 1, '0123456789', 'Khuat Van Thanh Danh', 1, '1990-01-01', NOW()),
('student@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'STUDENT', 1, '0123456788', 'Nguyen Van A', 1, '2005-09-01', NOW()),
('teacher@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'TEACHER', 1, '0123456787', 'Tran Thi B', 0, '1985-06-10', NOW()),
('counselor@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'COUNSELOR', 1, '0123456786', 'Pham Van C', 1, '1988-03-15', NOW()),
('parent@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'PARENTS', 1, '0123456785', 'Le Thi D', 0, '1975-12-20', NOW());


-- Insert Teacher
INSERT INTO teachers (id, teacher_code)
VALUES (3, 3);

-- Insert Student
INSERT INTO students (id, student_code, is_enable_survey)
VALUES (2, 2,  1);

-- Insert Counselor
INSERT INTO counselors (id, counselor_code)
VALUES (4, 'C001');

-- Insert Guardian
INSERT INTO guardians (id, address)
VALUES (5,  '123 ABC Street');


-- Insert Relationship
INSERT INTO relationship (student_id, guardian_id, relationship_type)
VALUES (2, 5, 'PARENT');

-- CATEGORY
INSERT INTO categories (code, description, is_active, is_limited, is_sum, max_score, min_score, name, question_length, severity_weight)
VALUES
    ('EMO', 'Emotional Health', b'1', b'0', b'1', 100, 0, 'Emotional', 10, 1.5),
    ('SOC', 'Social Behavior', b'1', b'1', b'0', 50, 10, 'Social', 5, 1.0);

-- LEVEL
INSERT INTO levels (code, description, intervention_required, label, level_type, max_score, min_score, symptoms_description, category_id)
VALUES
    ('L1', 'Low concern', 'None', 'Low', 1, 30, 0, 'Minimal symptoms', 1),
    ('L2', 'Moderate concern', 'Optional', 'Medium', 1, 70, 31, 'Some noticeable signs', 1),
    ('L3', 'High concern', 'Immediate', 'High', 1, 100, 71, 'Severe symptoms', 1);

-- CLASS
INSERT INTO classes (code_class, end_time, grade, is_active, school_year, start_time, teacher_id)
VALUES
    ('10A1', '2025-05-31 00:00:00', 'GRADE_10', b'1', '2024-2025', '2024-09-01 00:00:00', NULL),
    ('11A2', '2025-05-31 00:00:00', 'GRADE_11', b'1', '2024-2025', '2024-09-01 00:00:00', NULL);

-- ENROLLMENT
INSERT INTO enrollment (class_id, student_id)
VALUES
    (1, 2);

-- CASE
# INSERT INTO cases (created_date, updated_date, description, priority, progress_trend, status, title, counselor_id, create_by, current_level_id, initial_level_id, student_id)
# VALUES
#     (NOW(), NULL, 'Student showing signs of stress.', 'HIGH', 'DECLINED', 'NEW', 'Stress Case', 1, 1, 2, 1, 5),
#     (NOW(), NULL, 'Student with improved focus.', 'LOW', 'IMPROVED', 'IN_PROGRESS', 'Focus Case', 2, 2, 1, 1, 6);

-- SURVEY
INSERT INTO survey (created_date, updated_date, description, end_date, is_recurring, is_required, recurring_cycle, start_date, status, survey_type, target_grade_level, target_scope, title, category_id, account_id)
VALUES
    (NOW(), NULL, 'Quarterly emotional health survey', '2025-03-30', b'1', b'1', 'MONTHLY' , '2025-03-01', 'PUBLISHED', 'SCREENING', 'GRADE_10', 'GRADE', 'Emotional Health Q1', 1, 1),
    (NOW(), NULL, 'Follow-up survey on social skills', '2025-04-15', b'0', b'1', 'WEEKLY', '2025-04-01', 'DRAFT', 'FOLLOWUP', 'GRADE_11', 'GRADE', 'Social Skills Follow-up', 2, 2);

INSERT INTO questions (is_active, is_required, survey_id, created_date, description, text, question_type)
VALUES
    (1, 1, 1,NOW(), 'How often do you feel anxious?', 'Do you often feel anxious?', 'LINKERT_SCALE'),
    (1, 1, 1, NOW(), 'How is your sleep quality?', 'Rate your sleep quality', 'LINKERT_SCALE');
-- Answers for question 1
INSERT INTO answers (question_id, score, text)
VALUES
    (1, 1, 'Never'),
    (1, 2, 'Rarely'),
    (1, 3, 'Sometimes'),
    (1, 4, 'Often'),
    (1, 5, 'Always');

-- Answers for question 2
INSERT INTO answers (question_id, score, text)
VALUES
    (2, 1, 'Very poor'),
    (2, 2, 'Poor'),
    (2, 3, 'Average'),
    (2, 4, 'Good'),
    (2, 5, 'Excellent');

#
# Quartz seems to work best with the driver mm.mysql-2.0.7-bin.jar
#
# PLEASE consider using mysql with innodb tables to avoid locking issues
#
# In your Quartz properties file, you'll need to set
# org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#


commit;