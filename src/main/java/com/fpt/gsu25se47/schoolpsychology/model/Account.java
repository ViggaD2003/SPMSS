package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.common.Auditable;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends Auditable implements UserDetails {

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

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private String fullName;

    private Boolean gender;

    private LocalDate dob;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Counselor counselor;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Teacher teacher;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Guardian guardian;

    @OneToMany(mappedBy = "student")
    private List<MentalEvaluation> mentalEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<ProgramParticipants> programRegistrations = new ArrayList<>();

    @OneToMany(mappedBy = "bookedFor")
    private List<Appointment> appointmentsForMe = new ArrayList<>();

    @OneToMany(mappedBy = "bookedBy")
    private List<Appointment> appointmentsMade = new ArrayList<>();

    @OneToMany(mappedBy = "hostedBy")
    private List<Slot> slotsHostBy = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<SurveyRecord> surveyRecords = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Token> tokens = new ArrayList<>();

    @OneToMany(mappedBy = "createBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Survey> surveys = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Notifications> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cases> cases = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status;
    }
}