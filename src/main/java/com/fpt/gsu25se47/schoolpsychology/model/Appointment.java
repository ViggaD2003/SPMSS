package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.HostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "booked_for")
    private Account bookedFor;

    @ManyToOne
    @JoinColumn(name = "booked_by")
    private Account bookedBy;

    @ManyToOne
    @JoinColumn(name = "hosted_by")
    private Account hostedBy;

    @Enumerated(EnumType.STRING)
    private HostType hostType;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private Boolean isOnline;

    private LocalDate createAt;

    private LocalDate updateAt;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private List<AppointmentRecord> appointmentRecords = new ArrayList<>();
}

