package com.hanaro.triptogether.exchangeRate.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "Exchange_rate")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long curIdx;

    @Column(nullable = false, length = 10)
    private String curCd;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal rate;

    public void  updateExchangeRate(BigDecimal curRate){
        this.rate = curRate;
    }


}
