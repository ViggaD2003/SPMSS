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
