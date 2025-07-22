package com.fpt.gsu25se47.schoolpsychology.model;

import com.assignment.test.common.Auditable;
import com.assignment.test.model.enums.ProgramStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "support_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportProgram extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Integer maxParticipants;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ProgramStatus status;

    private String linkMeet;

    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "hosted_by", nullable = false)
    private Account hostedBy;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;


    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramParticipants> programRegistrations = new ArrayList<>();

}
