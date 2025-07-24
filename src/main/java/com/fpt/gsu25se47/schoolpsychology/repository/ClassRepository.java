package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Classes,Integer> {
//    Classes findByClassName(String className);
//    Optional<Classes> findByCodeClass(String code);

    @Query("SELECT e.classes FROM Enrollment e " +
            "JOIN e.student s " +
            "WHERE e.classes.isActive = true AND s.id = :studentId")
    Classes findActiveClassByStudentId(@Param("studentId") Integer studentId);

}
