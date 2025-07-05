package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("SELECT a FROM Appointment a WHERE a.bookedBy.id = :bookedById")
    List<Appointment> findByBookedBy(Integer bookedById);

    @Query("SELECT a FROM Appointment a WHERE a.slot.hostedBy.id = :hostId")
    List<Appointment> findAllBySlotHostedBy(@Param("hostId") Integer hostId);

    @Query("SELECT a FROM Appointment a WHERE a.status = :status and a.endDateTime < :now")
    List<Appointment> findAllAppointmentExpired(AppointmentStatus status, LocalDateTime now);

    @Query("SELECT a FROM Appointment a WHERE a.slot.id = :slotId AND a.status <> 'CANCELED'")
    List<Appointment> findAllBySlotId(Integer slotId);
}
