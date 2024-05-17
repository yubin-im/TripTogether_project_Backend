package com.hanaro.triptogether.exchangeRate;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Exchange_rate")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long curIdx;

    @Column(nullable = false, length = 10)
    private String curCd;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal rate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;
    private LocalDateTime deletedAt;
}
