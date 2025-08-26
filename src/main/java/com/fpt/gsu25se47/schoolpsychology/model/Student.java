package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Student {

    @Id
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

    @Column(nullable = false, unique = true)
    private String studentCode;

    private Boolean isEnableSurvey;

    @Enumerated(EnumType.STRING)
    private Grade targetLevel;

    @OneToMany(mappedBy = "student")
    private List<Relationship> relationships = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Enrollment> enrollments = new ArrayList<>();

}