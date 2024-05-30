package com.hanaro.triptogether.exchangeRate.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateInfoResponseDto {

    private String exchangeRateTime;
    private  List<ExchangeRateResponse> exchangeRates;

}

