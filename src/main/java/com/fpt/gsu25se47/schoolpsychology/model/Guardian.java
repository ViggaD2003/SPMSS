package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guardians")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@ToString(exclude = {"account"}) // 👈 thêm dòng này
public class Guardian {

    @Id
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @MapsId // dùng id của account luôn
    @JoinColumn(name = "id") // dùng chung cột "id"
    private Account account;

    private String address;

    @OneToMany(mappedBy = "guardian")
    private List<Relationship> relationships = new ArrayList<>();
}