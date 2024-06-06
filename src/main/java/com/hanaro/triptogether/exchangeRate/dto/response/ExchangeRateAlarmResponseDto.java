package com.hanaro.triptogether.exchangeRate.dto.response;

import com.hanaro.triptogether.enumeration.ExchangeRateAlarmType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ExchangeRateAlarmResponseDto {
    private String curCode;
    private String curName;
    private BigDecimal curRate;
    private ExchangeRateAlarmType curType;

}
