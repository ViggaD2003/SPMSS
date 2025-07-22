package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @MapsId // dùng id của account luôn
    @JoinColumn(name = "id") // dùng chung cột "id"
    private Account account;

    @Column(nullable = false, unique = true)
    private String teacherCode;

    private String linkMeet;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Classes> classes = new ArrayList<>();
}