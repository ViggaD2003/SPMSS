package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SupportProgramRepository extends JpaRepository<SupportProgram, Integer> {

    @Query("""
            SELECT sp
            FROM SupportProgram sp
                     LEFT JOIN ProgramParticipants pp
                               ON sp.id = pp.program.id AND pp.student.id = :studentId
            WHERE sp.status = 'ACTIVE'
              AND pp.id IS NULL
            """)
    List<SupportProgram> findAllActive(Integer studentId);

    @Query("SELECT sp FROM SupportProgram sp WHERE sp.hostedBy.id = :hostedById")
    List<SupportProgram> findAllByHostedBy(Integer hostedById);

    @Query("""
            SELECT sp FROM SupportProgram sp
            WHERE sp.category.id = :categoryId AND sp.status = 'ACTIVE'
            """)
    List<SupportProgram> recommendSupportPrograms(Integer categoryId);

    @Query("""
                SELECT sp
                FROM SupportProgram sp
                JOIN ProgramParticipants pp ON sp.id = pp.program.id
                WHERE pp.student.id = :studentId
            """)
    List<SupportProgram> findByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT sp " +
            "FROM ProgramParticipants pp " +
            "JOIN pp.program sp " +
            "WHERE (pp.student.id = :userId OR sp.hostedBy.id = :userId) " +
            "AND sp.startTime < :appointmentEndTime " +
            "AND sp.endTime > :appointmentStartTime " +
            "AND (sp.status = 'ACTIVE' OR sp.status = 'ON_GOING') " +
            "AND pp.status = 'ENROLLED'")
    List<SupportProgram> findConflictingProgramsWithAppointment(
            @Param("userId") Integer userId,
            @Param("appointmentStartTime") LocalDateTime appointmentStartTime,
            @Param("appointmentEndTime") LocalDateTime appointmentEndTime
    );

    @Query("""

            SELECT CASE WHEN COUNT(sp) > 0 THEN 1 ELSE 0 END
           FROM SupportProgram sp
           WHERE sp.hostedBy.id = :userId
             AND sp.status IN ('ACTIVE','ON_GOING')
             AND sp.startTime >= :startOfDay
             AND sp.endTime < :endOfDay
           """)
    int countProgramsForCounselorByDate(@Param("userId") Integer userId,
                                        @Param("startOfDay") LocalDateTime startDate,
                                        @Param("endOfDay") LocalDateTime endDate);
}
