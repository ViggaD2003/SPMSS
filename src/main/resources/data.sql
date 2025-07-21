INSERT INTO accounts (email, password, role, status, phone_number, full_name, gender, dob, created_date)
VALUES
-- Existing records
('danhkvtse172932@fpt.edu.vn', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'MANAGER', 1, '0123456789', 'Khuat Van Thanh Danh', 1, '1990-01-01', NOW()),
('student@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'STUDENT', 1, '0123456788', 'Nguyen Van A', 1, '2005-09-01', NOW()),
('teacher@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'TEACHER', 1, '0123456787', 'Tran Thi B', 0, '1985-06-10', NOW()),
('counselor@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'COUNSELOR', 1, '0123456786', 'Pham Van C', 1, '1988-03-15', NOW()),
('parent@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'PARENTS', 1, '0123456785', 'Le Thi D', 0, '1975-12-20', NOW());

-- Insert Classes
INSERT INTO classes (code_class, class_year) VALUES ('SE1800', '2024-01-01');

-- Insert Teacher
INSERT INTO teachers (id, account_id, teacher_code, class_id)
VALUES (3, 3, 'T001',  1);

-- Insert Student
INSERT INTO students (id, account_id, student_code, class_id,  is_enable_survey)
VALUES (2, 2, 'S001', 1, 1);

-- Insert Counselor
INSERT INTO counselors (id, account_id, counselor_code)
VALUES (4, 4, 'C001');

-- Insert Guardian
INSERT INTO guardians (id, account_id, address)
VALUES (5, 5, '123 ABC Street');

-- Insert Relationship
INSERT INTO relationship (student_id, guardian_id, relationship_type)
VALUES (2, 5, 'PARENT');

INSERT INTO categories (id, created_date, code, name)
VALUES
    (1, NOW(), 'MENTAL_HEALTH', 'Mental Health'),
    (2, NOW(), 'ACADEMIC_PERFORMANCE', 'Academic Performance');

INSERT INTO sub_type (category_id, length, limited_questions, code_name, description)
VALUES
    (1, 30, 0, 'GAD-7', 'Basic mental evaluation'),
    ( 1, 20, 1, 'PHQ-8', 'Basic academic evaluation');


INSERT INTO levels (level_name, max_score, min_score, sub_type_id, label)
VALUES
    (1, 10, 0, 1, 'Low'),
    (2, 20, 11, 1, 'Medium'),
    (3, 30, 21, 1, 'High'),
    (4, 10, 0, 2, 'Low'),
    (5, 20, 11, 2, 'Medium'),
    (6, 30, 21, 2, 'High');
INSERT INTO survey (id, account_id, is_recurring, is_required, round, start_date, end_date, created_date, name, description, survey_code, status)
VALUES
    (1, 1, 0, 1, 1, '2025-07-01', '2025-07-31', NOW(), 'Mental Health Survey July', 'Survey for mental health status', 'GAD-7', 'PUBLISHED');
INSERT INTO questions (is_active, is_required, survey_id, sub_type_id, created_date, description, text, module_type, question_type)
VALUES
    (1, 1, 1, 1, NOW(), 'How often do you feel anxious?', 'Do you often feel anxious?', 'SURVEY', 'LINKERT_SCALE'),
    (1, 1, 1, 1, NOW(), 'How is your sleep quality?', 'Rate your sleep quality', 'SURVEY', 'LINKERT_SCALE');
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
