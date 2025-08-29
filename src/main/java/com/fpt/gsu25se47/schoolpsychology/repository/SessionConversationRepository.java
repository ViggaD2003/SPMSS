package com.fpt.gsu25se47.schoolpsychology.repository;

import com.fpt.gsu25se47.schoolpsychology.model.ai.SessionConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SessionConversationRepository extends JpaRepository<SessionConversation, Integer> {

    boolean existsByConversationId(String conversationId);

    @Query(value = """
            select sacm.* from SPRING_AI_CHAT_MEMORY sacm
            join session_conversation sc on sc.conversation_id  = sacm.conversation_id
            join ai_session as2 on as2.id = sc.session_id
            where as2.id = '3571f41c-ce15-41fc-999c-bb5eb8b1bd3f'
            order by sacm.`timestamp` desc
        """, nativeQuery = true)
    List<Object[]> getAllMessagesBySessionId(String sessionId);
}
