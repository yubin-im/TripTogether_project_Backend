package com.hanaro.triptogether.exchangeRate.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ExchangeRateResponse {
    private String curCode;
    private String curName;
    private String curIcon;
    private String curRate;
}
