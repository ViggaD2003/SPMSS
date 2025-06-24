-- Insert Accounts
INSERT INTO accounts (email, password, role, status, phone_number, created_date)
VALUES
    ('manager@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'MANAGER', 1, '0123456789', NOW()),
    ('student@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'STUDENT', 1, '0123456788', NOW()),
    ('teacher@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'TEACHER', 1, '0123456787', NOW()),
    ('counselor@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'COUNSELOR', 1, '0123456786', NOW()),
    ('parent@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'PARENTS', 1, '0123456785', NOW());

-- Insert Classes
INSERT INTO classes (code_class, class_year) VALUES ('SE1800', '2024-01-01');

-- Insert Teacher
INSERT INTO teachers (id, account_id, teacher_code, full_name, gender, class_id)
VALUES (3, 3, 'T001', 'Teacher Name', 1, 1);

-- Insert Student
INSERT INTO students (id, account_id, student_code, full_name, gender, dob, is_enable_survey)
VALUES (2, 2, 'S001', 'Student Name', 1, '2005-05-10', 1);

-- Insert Counselor
INSERT INTO counselors (id, account_id, counselor_code, full_name, gender)
VALUES (4, 4, 'C001', 'Counselor Name', 0);

-- Insert Guardian
INSERT INTO guardians (id, account_id, full_name, gender, address)
VALUES (5, 5, 'Parent Name', 0, '123 ABC Street');

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
    recurring_cycle, start_date, end_date, created_date, account_id
)
VALUES (
           'Khảo sát tâm lý học đường',
           'Khảo sát đánh giá tâm lý học sinh',
           1, 1, 0, 'MONTHLY',
           '2024-01-01', '2024-12-31',
           NOW(), 1
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
