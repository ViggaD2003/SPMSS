package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CounselorRepository extends JpaRepository<Counselor, Integer> {

    @Query("SELECT c.counselorCode FROM Counselor c ORDER BY c.counselorCode DESC LIMIT 1")
    String findTopCounselorCode();
}
