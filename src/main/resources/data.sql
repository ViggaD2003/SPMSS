-- Insert Accounts
INSERT INTO accounts (email, password, role, status, phone_number, full_name, gender, dob, created_date)
VALUES
-- Existing records
('manager@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'MANAGER', 1, '0123456789', 'Khuat Van Thanh Danh', 1, '1990-01-01', NOW()),
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

-- Dummy data for Category
INSERT INTO categories (name, code, created_date)
VALUES
    ('Tâm lý học đường', 'PSY',  NOW()),
    ('Kỹ năng sống', 'LIFE',  NOW());

-- Dummy data for Level
INSERT INTO levels (text, description, min_score, max_score, is_required, category_id)
VALUES
    ('Bình thường', 'Không có dấu hiệu bất thường', 0, 5, 1, 1),
    ('Cần hỗ trợ', 'Có dấu hiệu cần hỗ trợ', 6, 10, 1, 1),
    ('Ổn định', 'Kỹ năng tốt', 0, 5, 1, 2),
    ('Cần cải thiện', 'Kỹ năng cần cải thiện', 6, 10, 1, 2);

-- Dummy data for Survey
INSERT INTO survey (
    name, description, status, is_required, is_recurring,
    recurring_cycle, start_date, end_date, created_date, account_id, survey_code
)
VALUES (
           'Khảo sát tâm lý học đường',
           'Khảo sát đánh giá tâm lý học sinh',
           1, 1, 0, 'MONTHLY',
           '2024-01-01', '2024-12-31',
           NOW(), 1, 'FAMILY_ENV'
       );

-- Dummy Question
INSERT INTO questions (
    text, description, is_active, question_type, module_type,
    is_required, survey_id, category_id, created_date
)
VALUES (
           'Bạn có thường xuyên cảm thấy bị quá tải với khối lượng bài tập và kỳ vọng học tập không?',
           'Đánh giá mức độ áp lực từ việc học.',
           1, 'MULTIPLE_CHOICE', 'SURVEY',
           1, 1, 1, NOW()
       );

-- Dummy Answers
INSERT INTO answers (text, score, question_id)
VALUES
    ('Có', 1, 1),
    ('Không', 0, 1);
