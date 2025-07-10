package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.AppointmentRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRecordRepository extends JpaRepository<AppointmentRecord, Integer> {

    @Query("""
            SELECT ar FROM AppointmentRecord ar
            JOIN Appointment m ON m.id = ar.appointment.id
            WHERE m.bookedFor.id = :accountId
            """)
    Page<AppointmentRecord> findAllByBookedFor(int accountId, Pageable pageable);

    @Query("""
            SELECT ar FROM AppointmentRecord ar
            JOIN Appointment m ON m.id = ar.appointment.id
            WHERE m.bookedBy.id = :accountId
            """)
    Page<AppointmentRecord> findAllByBookedBy(int accountId, Pageable pageable);
//    @Query("""
//            SELECT ar FROM AppointmentRecord ar
//            JOIN Appointment m ON m.id = ar.appointment.id
//            WHERE m.hostedBy.id = :accountId
//            """)
//    public List<AppointmentRecord> findAllByHostBy(int accountId);


    boolean existsByAppointmentId(int appointmentId);

    List<AppointmentRecord> findAllByAppointmentId(Integer appointmentId);
}
