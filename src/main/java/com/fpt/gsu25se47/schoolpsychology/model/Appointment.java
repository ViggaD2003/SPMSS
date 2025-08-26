package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.common.Auditable;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.HostType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SessionFlow;
import com.fpt.gsu25se47.schoolpsychology.model.enums.StudentCoopLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "booked_for")
    private Account bookedFor;

    @ManyToOne
    @JoinColumn(name = "booked_by")
    private Account bookedBy;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Cases cases;

    private Boolean isOnline;

    private String location;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private HostType hostType;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private String sessionNotes;

    private String reasonBooking;

    private String noteSummary;

    private String noteSuggestion;

    private String cancelReason;

    @Enumerated(EnumType.STRING)
    private SessionFlow sessionFlow;

    @Enumerated(EnumType.STRING)
    private StudentCoopLevel studentCoopLevel;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<AssessmentScores> assessmentScores = new ArrayList<>();

    @OneToOne(mappedBy = "appointment")
    private MentalEvaluation mentalEvaluation;
}

