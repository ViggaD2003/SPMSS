package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("SELECT t.teacherCode FROM Teacher t ORDER BY t.teacherCode DESC LIMIT 1")
    String findTopTeacherCode();
    Optional<Teacher> findByTeacherCode(String teacherCode);

    @Query("""
            SELECT t
            FROM Teacher t
            JOIN t.account a
            WHERE a.status = true
            AND NOT EXISTS (
                SELECT 1
                FROM Classes c
                WHERE c.teacher = t
                AND c.isActive = true
            )
            """)
    List<Teacher> findEligibleStudents(@Param("classId") Integer classId);
}
