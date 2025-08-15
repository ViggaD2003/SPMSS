package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query("SELECT s.studentCode FROM Student s ORDER BY s.studentCode DESC LIMIT 1")
    String findTopStudentCode();

    Optional<Student> findByStudentCode(String studentCode);

//    @Query("""
//            SELECT s FROM Student s
//            JOIN s.account a
//            LEFT JOIN Enrollment e on e.student.id = s.id
//            LEFT JOIN e.classes c
//            WHERE a.status = true
//            AND NOT EXISTS (
//                  SELECT 1
//                  FROM Enrollment e2
//                  JOIN e2.classes c2
//                  WHERE e2.student = s
//                    AND c2.isActive = true
//            )
//            AND (:grade IS NULL OR c.grade = :grade)
//            AND (:schoolYear IS NULL OR c.schoolYear = :schoolYear)
//            AND (:classCode IS NULL OR c.codeClass = :classCode)
//            """)
//    List<Student> findEligibleStudents(
//            @Param("grade") Grade grade,
//            @Param("schoolYearId") Integer schoolYearId,
//            @Param("classCode") String classCode);

    @Query("""
            SELECT s
            FROM Student s
            JOIN Account a ON a.id = s.id
            WHERE a.status = true
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
            """)
    List<Student> findEligibleStudents(@Param("classId") Integer classId);

}
