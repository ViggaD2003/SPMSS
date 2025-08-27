package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.cases.id = :casesId")
    List<ChatRoom> findAllByCasesId(Integer casesId);


    @Query("SELECT cr FROM ChatRoom cr WHERE cr.cases.id = :casesId AND cr.roleRoom = :roleRoom AND cr.email = :email")
    ChatRoom findAllByCasesIdAndByRoleRoom(Integer casesId, String roleRoom, String email);
}
