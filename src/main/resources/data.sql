-- Assuming the database is clean and IDs start from 1 for auto-incremented tables.
-- The password for all users is "123456". The bcrypt hash is: $2a$10$N0iB65LA5F.Pa1s27gTfM.oAHe1H.s24gIu.ShlD2h2S3m.YagL22

-- Insert Accounts
-- IDs are auto-generated, so we assume the order: 1:Manager, 2:Student, 3:Teacher, 4:Counselor, 5:Parent
INSERT INTO accounts (email, password, role, status, phone_number, created_date) VALUES ('manager@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'MANAGER', 1, '0123456789', NOW());
INSERT INTO accounts (email, password, role, status, phone_number, created_date) VALUES ('student@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'STUDENT', 1, '0123456788', NOW());
INSERT INTO accounts (email, password, role, status, phone_number, created_date) VALUES ('teacher@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'TEACHER', 1, '0123456787', NOW());
INSERT INTO accounts (email, password, role, status, phone_number, created_date) VALUES ('counselor@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'COUNSELOR', 1, '0123456786', NOW());
INSERT INTO accounts (email, password, role, status, phone_number, created_date) VALUES ('parent@school.com', '$2a$10$jJWwV0NgBmkJYF1VgmOm2O1fFth2U7HN9RWHf5Bee7/rSuDWkBUnC', 'PARENTS', 1, '0123456785', NOW());

-- Insert Classes
-- ID will be 1
INSERT INTO classes (code_class, class_year) VALUES ('SE1800', '2024-01-01');

-- Insert Teacher (linking to account_id=3 and class_id=1)
INSERT INTO teachers (id, account_id, teacher_code, full_name, gender, class_id) VALUES (3, 3, 'T001', 'Teacher Name', 1, 1);

-- Insert Student (linking to account_id=2 and class_id=1)
INSERT INTO students (id, account_id, student_code, full_name, gender, dob, is_enable_survey) VALUES (2, 2, 'S001', 'Student Name', 1, '2005-05-10', 1);

-- Insert Counselor (linking to account_id=4)
INSERT INTO counselors (id, account_id, counselor_code, full_name, gender) VALUES (4, 4, 'C001', 'Counselor Name', 0);

-- Insert Guardian (linking to account_id=5)
INSERT INTO guardians (id, account_id, full_name, gender, address) VALUES (5, 5, 'Parent Name', 0, '123 ABC Street');

-- Insert Relationship (linking student_id=2 and guardian_id=5)
INSERT INTO relationship (student_id, guardian_id, relationship_type) VALUES (2, 5, 'PARENT');

-- Dummy data for Category
INSERT INTO categories (name, code, created_date) VALUES ('Tâm lý học đường', 'PSY', NOW());
INSERT INTO categories (name, code, created_date) VALUES ('Kỹ năng sống', 'LIFE', NOW());

-- Dummy data for Level (liên kết với category)
INSERT INTO levels (text, description, min_score, max_score, is_required, category_id) VALUES ('Bình thường', 'Không có dấu hiệu bất thường', 0, 5, 1, 1);
INSERT INTO levels (text, description, min_score, max_score, is_required, category_id) VALUES ('Cần hỗ trợ', 'Có dấu hiệu cần hỗ trợ', 6, 10, 1, 1);
INSERT INTO levels (text, description, min_score, max_score, is_required, category_id) VALUES ('Ổn định', 'Kỹ năng tốt', 0, 5, 1, 2);
INSERT INTO levels (text, description, min_score, max_score, is_required, category_id) VALUES ('Cần cải thiện', 'Kỹ năng cần cải thiện', 6, 10, 1, 2);

-- Dummy data for Survey
INSERT INTO survey (name, description, status, is_required, is_recurring, recurring_cycle, start_date, end_date, created_date) VALUES ('Khảo sát tâm lý học đường', 'Khảo sát đánh giá tâm lý học sinh', 1, 1, 0, "MONTHLY", '2024-01-01', '2024-12-31', NOW());


INSERT INTO questions (
    text,
    description,
    is_active,
    question_type,
    module_type,
    is_required,
    survey_id,
    category_id,
    created_date
) VALUES (
             'Bạn có thường xuyên cảm thấy bị quá tải với khối lượng bài tập và kỳ vọng học tập không?',
             'Đánh giá mức độ áp lực từ việc học.',
             1,
             'MULTIPLE_CHOICE',
             'SURVEY',
             1,
             1,
             1,
             NOW()
         );

-- Dummy data for Answer (liên kết với question)
INSERT INTO answers (text, score, question_id) VALUES ('Có', 1, 1);
INSERT INTO answers (text, score, question_id) VALUES ('Không', 0, 1);
INSERT INTO answers (text, score, question_id) VALUES ('Thường xuyên', 1, 2);
INSERT INTO answers (text, score, question_id) VALUES ('Hiếm khi', 0, 2); 