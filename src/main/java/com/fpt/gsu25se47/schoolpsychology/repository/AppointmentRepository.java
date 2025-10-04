package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.SupportProgram;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.HostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("SELECT a FROM Appointment a WHERE a.status = :status and a.endDateTime < :now")
    List<Appointment> findAllAppointmentExpired(AppointmentStatus status, LocalDateTime now);

    @Query("""
                SELECT a FROM Appointment a
                WHERE
                    (a.bookedBy.id = :participantId OR a.bookedFor.id = :participantId)
                    AND a.startDateTime < :endDateTime
                    AND a.endDateTime > :startDateTime
                    AND a.status NOT IN ('CANCELED', 'ABSENT', 'COMPLETED')
            """)
    List<Appointment> findConflictingAppointments(
            Integer participantId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );

    @Query("""
                SELECT a FROM Appointment a
                WHERE a.bookedBy.id = :bookedById
                AND a.status IN (:statuses)
                ORDER BY a.startDateTime DESC
            """)
    List<Appointment> findByBookedByAndStatus(Integer bookedById,
                                              List<AppointmentStatus> statuses);

    List<Appointment> findAllByStatus(AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.cases.id =:caseId")
    List<Appointment> findAllByCaseId(Integer caseId);

    @Query("""
                SELECT a FROM Appointment a
                WHERE a.bookedFor.id = :bookedForId
                  AND a.status IN (:statuses)
                  AND (:startDateTime IS NULL OR a.startDateTime >= :startDateTime)
                  AND (:endDateTime IS NULL OR a.endDateTime <= :endDateTime)
                ORDER BY a.startDateTime ASC, a.endDateTime ASC
            """)
    List<Appointment> findByBookedForAndStatus(
            Integer bookedForId,
            List<AppointmentStatus> statuses,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );


    @Query("""
                SELECT a FROM Appointment a
                JOIN Slot s on s.id = a.slot.id
                WHERE s.hostedBy.id = :hostById AND a.status IN (:statuses)
            """)
    List<Appointment> findAllByHostByWithStatus(Integer hostById, List<AppointmentStatus> statuses);

    @Query("""
                SELECT a FROM Appointment a
                JOIN Slot s on s.id = a.slot.id
                WHERE s.hostedBy.id = :hostById AND a.status = 'CONFIRMED'
            """)
    List<Appointment> findAllByHostByWithStatusConfirmed(Integer hostById);

    @Query("""
                SELECT COUNT(a)
                FROM Appointment a
                WHERE a.bookedBy.id = :bookedById
                  AND a.startDateTime BETWEEN :start AND :end
                  AND
                  (a.status <> 'CANCELED' AND a.status <> 'ABSENT' AND a.status <> 'COMPLETED')
            """)
    int countActiveAppointmentsInRange(
            @Param("bookedById") Integer bookedById,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("cancelStatus") AppointmentStatus cancelStatus
    );

    @Query("""
                SELECT COUNT(a)
                FROM Appointment a
                WHERE a.bookedBy.id = :bookedById
                  AND a.hostType = :hostType
                  AND a.startDateTime BETWEEN :start AND :end
                  AND
                  (a.status <> 'CANCELED' AND a.status <> 'ABSENT' AND a.status <> 'COMPLETED')
            """)
    int countActiveAppointmentsStudentsInRange(
            @Param("bookedById") Integer bookedById,
            @Param("hostType") HostType hostType,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
            SELECT DISTINCT a.*
            FROM appointment a
            JOIN cases c ON a.case_id = c.id
            JOIN levels l ON c.current_level_id = l.id  -- hoặc initial_level_id
            JOIN categories cat ON l.category_id = cat.id
            WHERE cat.id = :categoryId
            """, nativeQuery = true)
    List<Appointment> findAllAppointmentWithCategory(Integer categoryId);


    @Query("""
            SELECT a FROM Appointment a
            JOIN a.slot s
            WHERE s.hostedBy.id = :counselorId
            AND YEAR(a.startDateTime) = YEAR(CURRENT_DATE)
            AND MONTH(a.startDateTime) = MONTH(CURRENT_DATE)
            ORDER BY a.startDateTime DESC
            """)
    List<Appointment> findMyHostedAppointmentsThisMonth(@Param("counselorId") Integer counselorId);


    @Query("SELECT a FROM Appointment a WHERE a.bookedFor.id = :bookedForId")
    List<Appointment> findAllByBookedFor(Integer bookedForId);


    @Query("""
            SELECT DISTINCT a
            FROM Appointment a
            WHERE a.bookedFor.id = :studentId
              AND a.status IN ('PENDING','CONFIRMED','IN_PROGRESS')
              AND a.startDateTime < :programStartTime
              AND a.endDateTime   > :programEndTime
            """)
    List<Appointment> findConflictingAppointmentsForStudent(
            @Param("studentId") Integer studentId,
            @Param("appointmentStartTime") LocalDateTime programStartTime,
            @Param("appointmentEndTime") LocalDateTime programEndTime
    );

}
