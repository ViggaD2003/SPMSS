package com.fpt.gsu25se47.schoolpsychology.model;

import com.assignment.test.common.Auditable;
import com.assignment.test.model.enums.Priority;
import com.assignment.test.model.enums.ProgressTrend;
import com.assignment.test.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cases extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private ProgressTrend progressTrend;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Account student;

    @ManyToOne
    @JoinColumn(name = "createBy")
    private Account createBy;

    @ManyToOne
    @JoinColumn(name = "counselor_id")
    private Account counselor;

    @ManyToOne
    @JoinColumn(name = "current_level_id")
    private Level currentLevel;

    @ManyToOne
    @JoinColumn(name = "initial_level_id")
    private Level initialLevel;

    @OneToMany(mappedBy = "cases", cascade = CascadeType.ALL)
    private List<ProgramParticipants> programParticipants;

    @OneToMany(mappedBy = "cases", cascade = CascadeType.ALL)
    private List<SurveyCaseLink> surveyCaseLinks;
}
