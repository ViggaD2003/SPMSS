package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "counselors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counselor {

    @Id
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false, unique = true)
    private String counselorCode;

    private String linkMeet;

    @Column(nullable = false)
    private String fullName;

    private Boolean gender;
}