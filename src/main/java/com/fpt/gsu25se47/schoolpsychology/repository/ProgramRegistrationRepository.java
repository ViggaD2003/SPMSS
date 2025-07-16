package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ProgramRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRegistrationRepository extends JpaRepository<ProgramRegistration, Integer> {

    int countByProgramId(Integer programId);

    boolean existsByProgramIdAndAccountId(Integer programId, Integer accountId);

    List<ProgramRegistration> findAllByProgramId(Integer programId);

    List<ProgramRegistration> findAllByAccountId(Integer accountId);
}
