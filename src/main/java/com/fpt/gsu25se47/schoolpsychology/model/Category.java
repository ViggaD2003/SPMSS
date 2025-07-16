package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String code;

    @OneToMany(mappedBy = "category")
    private List<SupportProgram> supportPrograms = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<MentalEvaluation> mentalEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<SubType> subTypes = new ArrayList<>();
}
