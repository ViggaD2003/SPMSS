package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.common.Auditable;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotUsageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String slotName;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Integer maxCases;

    private Boolean isBooked;

    private String location;

    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @Enumerated(EnumType.STRING)
    private SlotUsageType type;

    @OneToMany(mappedBy = "slot")
    private List<ProgramSession> programSessions = new ArrayList<>();
}
