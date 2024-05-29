package com.hanaro.triptogether.exchangeRate.dto.request;

import com.hanaro.triptogether.enumeration.ExchangeRateAlarmType;
import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRate;
import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRateAlarm;
import com.hanaro.triptogether.team.domain.Team;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ExchangeRateAlarmRequestDto {

    private Long teamIdx;
    private String curCode;
    private BigDecimal curRate;
    private ExchangeRateAlarmType rateAlarmType;
    private String fcmToken;

    public ExchangeRateAlarm toEntity(Team team, ExchangeRate exchangeRate) {
        return ExchangeRateAlarm.builder()
                .team(team)
                .fcmToken(fcmToken)
                .exchangeRate(exchangeRate)
                .rateType(rateAlarmType)
                .curCode(curCode)
                .curRate(curRate).build();
    }

}
