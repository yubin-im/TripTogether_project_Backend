package com.hanaro.triptogether.account;


import com.hanaro.triptogether.member.Member;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accIdx;

    @ManyToOne
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    @Column(nullable = false, length = 255)
    private String accNumber;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal accBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean cardStatus = false;

    @Column(nullable = false, length = 255)
    private String accName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;
    private LocalDateTime deletedAt;
}
