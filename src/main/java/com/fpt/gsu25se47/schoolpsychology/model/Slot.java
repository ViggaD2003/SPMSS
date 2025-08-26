package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated(value = EnumType.STRING)
    private SlotStatus status;

    @ManyToOne
    @JoinColumn(name = "hosted_by")
    private Account hostedBy;

    @OneToMany(mappedBy = "slot")
    private List<Appointment> appointments = new ArrayList<>();
}
