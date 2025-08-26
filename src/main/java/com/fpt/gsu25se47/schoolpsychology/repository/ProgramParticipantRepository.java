package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ProgramParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProgramParticipantRepository extends JpaRepository<ProgramParticipants, Integer> {

    @Query("SELECT pp FROM ProgramParticipants pp WHERE pp.program.id = :programId")
    List<ProgramParticipants> findByProgramId(Integer programId);

    @Query("""
    SELECT pp 
    FROM ProgramParticipants pp 
    WHERE pp.student.id = :studentId 
      AND pp.joinAt BETWEEN :startDate AND :endDate
""")
    List<ProgramParticipants> findByStudentIdAndJoinAtBetween(
            Integer studentId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );


    @Query("SELECT pp FROM ProgramParticipants pp WHERE pp.student.id =:studentId AND pp.program.id = :supportProgramId")
    ProgramParticipants findByStudentId(Integer studentId, Integer supportProgramId);

    @Query("""
    SELECT CASE 
               WHEN COUNT(sr.id) >= 2 THEN TRUE 
               ELSE FALSE 
           END
    FROM ProgramParticipants pp
    JOIN pp.program sp
    JOIN sp.survey s
    JOIN SurveyRecord sr ON sr.survey = s AND sr.student.id = pp.student.id
    WHERE pp.id = :participantId
    """)
    Boolean hasParticipantCompletedSurveyTwice(@Param("participantId") Integer participantId);


    @Query("SELECT pp FROM ProgramParticipants pp WHERE pp.cases.id = :caseId")
    List<ProgramParticipants> findAllByCaseId(Integer caseId);
}
