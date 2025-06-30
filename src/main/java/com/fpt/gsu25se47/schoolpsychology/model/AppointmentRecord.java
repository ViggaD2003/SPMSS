package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.common.Auditable;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RecordStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SessionFlow;
import com.fpt.gsu25se47.schoolpsychology.model.enums.StudentCoopLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointment_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRecord extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    private SessionFlow sessionFlow;

    @Enumerated(EnumType.STRING)
    private StudentCoopLevel studentCoopLevel;

    @Enumerated(EnumType.STRING)
    private RecordStatus status;

    private String noteSummary;

    private String noteSuggest;

    private Integer totalScore;

    @OneToMany(mappedBy = "appointmentRecord")
    private List<AnswerRecord> answerRecords = new ArrayList<>();
}
