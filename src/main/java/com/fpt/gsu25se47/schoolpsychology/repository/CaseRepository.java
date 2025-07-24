package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CaseRepository extends JpaRepository<Cases, Integer> {
    @Query("SELECT c FROM Cases c WHERE c.counselor.id =:counselorId")
    List<Cases> findAllByCounselorId(Integer counselorId);

    @Query("SELECT c FROM Cases c WHERE c.createBy.id =:teacherId")
    List<Cases> findAllByTeacherId(Integer teacherId);
}
