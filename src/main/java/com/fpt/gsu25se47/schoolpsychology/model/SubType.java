package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sub_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String codeName;

    private String description;

    private Boolean limitedQuestions;

    private Integer length;

    @OneToMany(mappedBy = "subType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Level> levels = new ArrayList<>();

    @OneToMany(mappedBy = "subType")
    private List<Question> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;



}
