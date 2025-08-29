package com.fpt.gsu25se47.schoolpsychology.model.ai;

import com.fpt.gsu25se47.schoolpsychology.model.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AiSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "created_time", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdTime;

    @Column(name = "edited_time", nullable = false)
    @UpdateTimestamp
    private LocalDateTime editedTime;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Account user;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionConversation> conversations;
}
