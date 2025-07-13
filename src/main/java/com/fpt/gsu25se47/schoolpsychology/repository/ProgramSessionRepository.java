package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ProgramSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramSessionRepository extends JpaRepository<ProgramSession, Integer> {

    List<ProgramSession> findAllByProgramId(Integer programId);
}
