package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guardians")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guardian {

    @Id
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private String fullName;

    private String address;

    private Boolean gender;

    @OneToMany(mappedBy = "guardian")
    private List<Relationship> relationships = new ArrayList<>();
}