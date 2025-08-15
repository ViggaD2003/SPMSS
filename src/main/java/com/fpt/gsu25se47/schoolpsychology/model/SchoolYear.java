package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "school_year")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String name;

    @OneToMany(mappedBy = "schoolYear", cascade = {CascadeType.ALL})
    private List<Term> terms = new ArrayList<>();
}
