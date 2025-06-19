package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean status;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "update_at")
    private LocalDate updateAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "hostBy")
    private List<ProgramSession> programSessions = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<ProgramRegistration> programRegistrations = new ArrayList<>();

    @OneToMany(mappedBy = "bookedFor")
    private List<Appointment> appointmentsForMe = new ArrayList<>();

    @OneToMany(mappedBy = "bookedBy")
    private List<Appointment> appointmentsMade = new ArrayList<>();

    @OneToMany(mappedBy = "hostedBy")
    private List<Appointment> appointmentsHosted = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<SurveyRecord> surveyRecords = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Token> tokens = new ArrayList<>();
}