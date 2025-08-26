package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "counselors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@ToString(exclude = {"account"}) // 👈 thêm dòng này
public class Counselor {

    @Id
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @MapsId // dùng id của account luôn
    @JoinColumn(name = "id") // dùng chung cột "id"
    private Account account;

    @Column(nullable = false, unique = true)
    private String counselorCode;

    private String linkMeet;

    private String eventId;
}