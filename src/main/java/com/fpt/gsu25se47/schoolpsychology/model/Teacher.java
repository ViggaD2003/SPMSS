package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @Column(name = "id")
    private Integer id; // PK & FK đến Account

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false, unique = true)
    private String teacherCode;

    private String linkMeet;

    @OneToOne
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Classes classes;
}