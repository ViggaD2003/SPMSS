package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("SELECT t.teacherCode FROM Teacher t ORDER BY t.teacherCode DESC LIMIT 1")
    String findTopTeacherCode();

}
