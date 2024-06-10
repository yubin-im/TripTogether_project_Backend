package com.hanaro.triptogether.exchangeRate.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ExchangeRateResponse {
    private String cur_code;
    private String cur_name;
    private String cur_icon;
    private String cur_rate;
}
