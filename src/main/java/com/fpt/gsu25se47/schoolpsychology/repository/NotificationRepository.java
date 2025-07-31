package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notifications, UUID> {

    @Query("SELECT nt FROM Notifications nt WHERE nt.receiver.id = :receiverId")
    List<Notifications> findAllByReceiverId(Integer receiverId);
}
