package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    @Query(value = """
            SELECT ch FROM ChatMessage ch WHERE ch.chatRoom.id = :chatRoomId
            """)
    List<ChatMessage> findAllByChatRoomId(Integer chatRoomId);

}
