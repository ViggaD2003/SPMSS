package com.fpt.gsu25se47.schoolpsychology.model.ai;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private AiSession session;

    @Column(name = "conversation_id", nullable = false, unique = true, length = 36)
    private String conversationId; // points to SPRING_AI_CHAT_MEMORY.conversation_id

    @Column(name = "created_time", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdTime;
}
