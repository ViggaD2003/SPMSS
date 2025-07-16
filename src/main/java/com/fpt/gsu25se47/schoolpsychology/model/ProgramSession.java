package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramSessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "program_session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private SupportProgram program;

//    @ManyToOne
//    @JoinColumn(name = "host_by", nullable = false)
//    private Account hostBy;

    private String topic;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProgramSessionStatus status;

    private LocalDate date;
}
