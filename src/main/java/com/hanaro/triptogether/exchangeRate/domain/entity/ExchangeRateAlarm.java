package com.hanaro.triptogether.exchangeRate.domain.entity;
import com.hanaro.triptogether.dues.domain.entity.BaseEntity;
import com.hanaro.triptogether.enumeration.ExchangeRateAlarmType;
import com.hanaro.triptogether.exchangeRate.dto.response.ExchangeRateAlarmResponseDto;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.team.domain.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "exchange_rate_alarm")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateAlarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_rate_alarm_idx")
    private Long alarmIdx;

    @ManyToOne
    @JoinColumn(name = "cur_idx", nullable = false)
    private ExchangeRate exchangeRate;

    @ManyToOne
    @JoinColumn(name = "member_idx", nullable = false)
    private Member member;

    @Column(name = "cur_rate", nullable = false)
    private BigDecimal curRate;

    private Boolean notified;

    @Enumerated(EnumType.STRING)
    @Column(name = "rate_type", nullable = false, columnDefinition = "ENUM('OVER', 'LESS')")
    private ExchangeRateAlarmType rateType;

    public void setNotified(boolean notify){
        this.notified = notify;
    }

    public ExchangeRateAlarmResponseDto toDto(ExchangeRateAlarm exchangeRateAlarm,ExchangeRate exchangeRate) {
        return ExchangeRateAlarmResponseDto.builder()
                .idx(exchangeRateAlarm.alarmIdx)
                .curIcon(exchangeRate.getCurIcon())
                .curCode(exchangeRate.getCurCode())
                .curType(exchangeRateAlarm.getRateType())
                .curRate(exchangeRateAlarm.getCurRate())
                .curName(exchangeRate.getCurName())
                .build();
    }


}
