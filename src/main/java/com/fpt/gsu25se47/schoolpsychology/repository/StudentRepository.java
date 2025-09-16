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
            SELECT s
            FROM Student s
            JOIN Account a ON a.id = s.id
            WHERE a.status = true
            AND (s.targetLevel IS NULL OR s.targetLevel = COALESCE(:grade, (
                SELECT c.grade
                FROM Classes c
                WHERE c.id = :classId
            )))
            AND (:gender IS NULL OR a.gender = :gender)
            AND NOT EXISTS (
                SELECT 1
                FROM Enrollment e
                JOIN e.classes c
                JOIN c.classesTerm ct
                JOIN ct.term t
                JOIN t.schoolYear sy
                WHERE e.student.id = s.id
                  AND c.isActive = true
                  AND sy.id IN (
                      SELECT DISTINCT sy2.id
                      FROM Classes c2
                      JOIN c2.classesTerm ct2
                      JOIN ct2.term t2
                      JOIN t2.schoolYear sy2
                      WHERE c2.id = :classId
                  )
            )
            ORDER BY a.fullName
            """)
    List<Student> findEligibleStudents(@Param("classId") Integer classId,
                                       @Param("grade") Grade grade,
                                       @Param("gender") Boolean gender);

}
