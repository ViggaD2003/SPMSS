package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@ToString(exclude = {"account"}) // 👈 thêm dòng này
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

    private String eventId;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Classes> classes = new ArrayList<>();
}