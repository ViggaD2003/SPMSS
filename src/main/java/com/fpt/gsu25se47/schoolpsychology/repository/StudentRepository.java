package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s.studentCode FROM Student s ORDER BY s.studentCode DESC LIMIT 1")
    String findTopStudentCode();

    Optional<Student> findByStudentCode(String studentCode);

    @Query("""
            SELECT s FROM Student s
            JOIN s.account a
            LEFT JOIN Enrollment e on e.student.id = s.id
            LEFT JOIN e.classes c
            WHERE a.status = true
            AND NOT EXISTS (
                  SELECT 1
                  FROM Enrollment e2
                  JOIN e2.classes c2
                  WHERE e2.student = s
                    AND c2.isActive = true
            )
            """)
    List<Student> findEligibleStudents(@Param("currentYear") int currentYear);

    @Query("""
            SELECT s FROM Student s
            JOIN s.account a
            LEFT JOIN Enrollment e on e.student.id = s.id
            LEFT JOIN e.classes c
            WHERE a.status = true
            AND NOT EXISTS (
                  SELECT 1
                  FROM Enrollment e2
                  JOIN e2.classes c2
                  WHERE e2.student = s
                    AND c2.isActive = true
            )
            AND (:grade IS NULL OR c.grade = :grade)
            AND (:schoolYear IS NULL OR c.schoolYear = :schoolYear)
            AND (:classCode IS NULL OR c.codeClass = :classCode)
            """)
    List<Student> findEligibleStudentsWithParams(
            @Param("grade") Grade grade,
            @Param("schoolYear") String schoolYear,
            @Param("classCode") String classCode,
            @Param("currentYear") int currentYear);
}
