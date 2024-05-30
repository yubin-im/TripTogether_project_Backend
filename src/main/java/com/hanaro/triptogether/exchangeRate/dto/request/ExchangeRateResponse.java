package com.hanaro.triptogether.exchangeRate.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ExchangeRateResponse {
    private String cur_unit; // 통화코드
    private String cur_name; //국가 이름
    private String deal_bas_r; // 매매 기준율
}
