package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.AppointmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRecordRepository extends JpaRepository<AppointmentRecord, Integer> {

    @Query("""
            SELECT ar FROM AppointmentRecord ar
            JOIN Appointment m ON m.id = ar.appointment.id
            WHERE m.bookedFor.id = :accountId
            """)
    public List<AppointmentRecord> findAllByBookedFor(int accountId);
    @Query("""
            SELECT ar FROM AppointmentRecord ar
            JOIN Appointment m ON m.id = ar.appointment.id
            WHERE m.bookedBy.id = :accountId
            """)
    public List<AppointmentRecord> findAllByBookedBy(int accountId);
    @Query("""
            SELECT ar FROM AppointmentRecord ar
            JOIN Appointment m ON m.id = ar.appointment.id
            WHERE m.hostedBy.id = :accountId
            """)
    public List<AppointmentRecord> findAllByHostBy(int accountId);
}
