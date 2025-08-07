package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
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
                    a.startDateTime < :endDateTime AND
                    a.endDateTime > :startDateTime AND
                    (a.status <> 'CANCELLED' AND a.status <> 'ABSENT' AND a.status <> 'COMPLETED')
            """)
    List<Appointment> findConflictingAppointments(LocalDateTime startDateTime, LocalDateTime endDateTime);

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
                ORDER BY a.startDateTime DESC
            """)
    List<Appointment> findByBookedForAndStatus(Integer bookedForId, List<AppointmentStatus> statuses);

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

    int countByBookedByIdAndStartDateTimeBetween(Integer bookedById, LocalDateTime start, LocalDateTime end);

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
}
