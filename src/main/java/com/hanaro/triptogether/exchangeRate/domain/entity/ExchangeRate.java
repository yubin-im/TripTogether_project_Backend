package com.hanaro.triptogether.exchangeRate.domain.entity;

import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateResponse;
import com.hanaro.triptogether.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rate")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long curIdx;

    @Column(nullable = false, length = 10)
    private String curCode;

    @Column(nullable = false, length = 10)
    private String curName;

    @Column( length = 10)
    private String curIcon;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal curRate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", insertable=false, updatable=false)
    private Member createdBy;

    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "last_modified_by", insertable=false, updatable=false)
    private Member lastModifiedBy;

    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "deleted_by", insertable=false, updatable=false)
    private Member deletedBy;

    public ExchangeRateResponse toDto(String cur_icon) {
        return ExchangeRateResponse.builder()
                .curCode(curCode)
                .curName(curName)
                .curIcon(cur_icon)
                .curRate(String.valueOf(curRate))
                .build();
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;

    }

    public void  updateExchangeRate(BigDecimal curRate){
        this.curRate = curRate;
    }


}
