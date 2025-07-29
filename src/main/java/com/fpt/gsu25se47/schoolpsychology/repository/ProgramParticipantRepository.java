package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ProgramParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramParticipantRepository extends JpaRepository<ProgramParticipants, Integer> {

    @Query("SELECT pp FROM ProgramParticipants pp WHERE pp.program.id = :programId")
    List<ProgramParticipants> findByProgramId(Integer programId);

    @Query("SELECT pp FROM ProgramParticipants pp WHERE pp.student.id =:studentId")
    ProgramParticipants findByStudentId(Integer studentId);
}
