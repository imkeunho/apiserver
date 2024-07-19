package com.junjin.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(
        name = "tbl_cart",
        indexes = {@Index(name = "idx_cart_email", columnList = "member_owner")}
)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cno;

    @OneToOne
    @JoinColumn(name = "member_owner")
    private Member owner;

}
