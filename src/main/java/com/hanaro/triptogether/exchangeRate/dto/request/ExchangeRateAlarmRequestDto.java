package com.hanaro.triptogether.exchangeRate.dto.request;

import com.hanaro.triptogether.common.BigDecimalConverter;
import com.hanaro.triptogether.enumeration.ExchangeRateAlarmType;
import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRate;
import com.hanaro.triptogether.exchangeRate.domain.entity.ExchangeRateAlarm;
import com.hanaro.triptogether.member.domain.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ExchangeRateAlarmRequestDto {

    private Long memberIdx;
    private String curCode;
    private String curRate;
    private ExchangeRateAlarmType rateAlarmType;

    public ExchangeRateAlarm toEntity(Member member, ExchangeRate exchangeRate) {
        return ExchangeRateAlarm.builder()
                .member(member)
                .exchangeRate(exchangeRate)
                .rateType(rateAlarmType)
                .notified(false)
                .curRate(BigDecimalConverter.convertStringToBigDecimal(curRate)).build();
    }

}
