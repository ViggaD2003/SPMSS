package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ProgramParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgramParticipantRepository extends JpaRepository<ProgramParticipants, Integer> {

    @Query("SELECT pp FROM ProgramParticipants pp WHERE pp.program.id = :programId")
    List<ProgramParticipants> findByProgramId(Integer programId);

    @Query("SELECT pp FROM ProgramParticipants pp WHERE pp.student.id =:studentId AND pp.program.id = :supportProgramId")
    ProgramParticipants findByStudentId(Integer studentId, Integer supportProgramId);

    @Query(value = """
    SELECT 
        CASE 
            WHEN COUNT(sr.id) >= 2 THEN TRUE
            ELSE FALSE
        END
    FROM program_participants pp
    JOIN support_program sp ON pp.program_id = sp.id
    JOIN survey s ON sp.survey_id = s.id
    JOIN survey_record sr ON sr.survey_id = s.id AND sr.account_id = pp.student_id
    WHERE pp.id = :participantId
    """, nativeQuery = true)
    boolean hasParticipantCompletedSurveyTwice(@Param("participantId") Integer participantId);

    @Query("SELECT pp FROM ProgramParticipants pp WHERE pp.cases.id = :caseId")
    List<ProgramParticipants> findAllByCaseId(Integer caseId);
}
