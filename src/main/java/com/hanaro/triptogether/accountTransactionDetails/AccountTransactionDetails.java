package com.hanaro.triptogether.accountTransactionDetails;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.dues.domain.entity.BaseEntity;
import com.hanaro.triptogether.enumeration.TransType;
import com.hanaro.triptogether.member.domain.Member;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_transaction_details")
public class AccountTransactionDetails extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transIdx;

    @ManyToOne
    @JoinColumn(name = "acc_idx", nullable = false)
    private Account account;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal transAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransType transType;

    @Column(nullable = false)
    private LocalDateTime transDate;

    @ManyToOne
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    @Column(nullable = false, length = 255)
    private String transFrom;

    @Column(nullable = false, length = 255)
    private String transTo;


}
