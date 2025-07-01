package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Integer> {

    @Query("SELECT s FROM Slot s WHERE s.hostedBy.id = :hostedById")
    List<Slot> findAllByHostedById(Integer hostedById);

    @Query("SELECT s FROM Slot s WHERE s.status = :status AND s.endDateTime < :now")
    List<Slot> findAllSlotsExpired(@Param("status") SlotStatus status, @Param("now") LocalDateTime now);


}
