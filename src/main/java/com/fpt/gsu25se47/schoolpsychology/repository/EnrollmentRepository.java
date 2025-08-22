package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Enrollment;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findAllByClassesIdAndStudentIdIn(Integer classId, List<Integer> studentIds);

    @Query("SELECT e.student FROM Enrollment e WHERE e.classes.id = :classId")
    List<Student> findStudentsByClassesId(@Param("classId") Integer classId);

    List<Enrollment> findAllByStudentIdIn(List<Integer> studentIds);

//    @Query(value = """
//            SELECT a.*
//            FROM enrollment e
//                     JOIN classes c ON c.id = e.class_id
//                     JOIN teachers t ON t.id = c.teacher_id
//                     JOIN students s ON s.id = e.student_id
//                     JOIN accounts a ON a.id = s.id
//            WHERE t.id = :teacherId
//              AND c.is_active = TRUE;
//            """, nativeQuery = true)
//    List<Account> findAllAccountByTeacherId(@Param("teacherId") Integer teacherId);
}
