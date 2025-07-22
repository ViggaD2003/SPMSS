package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ProgramParticipants;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRegistrationRepository extends JpaRepository<ProgramParticipants, Integer> {
}
