package com.hanaro.triptogether.accountTransactionDetails;

import com.hanaro.triptogether.account.Account;
import com.hanaro.triptogether.enumeration.TransType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Account_transaction_details")
public class AccountTransactionDetails {
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

    private String transName;

    @Column(nullable = false, length = 255)
    private String transFrom;

    @Column(nullable = false, length = 255)
    private String transTo;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;

}
