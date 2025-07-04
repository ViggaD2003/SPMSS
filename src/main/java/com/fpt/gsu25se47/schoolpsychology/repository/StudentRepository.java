package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s.studentCode FROM Student s ORDER BY s.studentCode DESC LIMIT 1")
    String findTopStudentCode();

    Student findByStudentCode(String studentCode);
}
