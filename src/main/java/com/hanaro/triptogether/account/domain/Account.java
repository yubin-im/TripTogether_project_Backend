package com.hanaro.triptogether.account.domain;


import com.hanaro.triptogether.member.domain.Member;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "Account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accIdx;

    @ManyToOne
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    @Column(nullable = false, length = 14)
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

    // 잔액 업데이트
    public void updateAccBalance(BigDecimal accBalance) {
        this.accBalance = accBalance;
    }

    // 수정날짜 업데이트
    public void updateModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
