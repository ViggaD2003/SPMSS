package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Enrollment;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    List<Enrollment> findAllByClassesIdAndStudentIdIn(Integer classId, List<Integer> studentIds);

    @Query("SELECT e.student FROM Enrollment e WHERE e.classes.id = :classId")
    List<Student> findStudentsByClassId(@Param("classId") Integer classId);
}
