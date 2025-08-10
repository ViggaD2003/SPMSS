package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value", length = 1024)
    private String value;

    private String status;

    private String tokenType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
