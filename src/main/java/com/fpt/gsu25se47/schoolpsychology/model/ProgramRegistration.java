package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.common.Auditable;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "program_registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramRegistration extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private SupportProgram program;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    private LocalDate registeredAt;
}
