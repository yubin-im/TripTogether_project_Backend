package com.hanaro.triptogether.exchangeRate.controller;

import com.hanaro.triptogether.common.response.BaseResponse;
import com.hanaro.triptogether.common.response.ResponseStatus;
import com.hanaro.triptogether.exchangeRate.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeService exchangeService;

    @GetMapping("/exchange-rate")
    public BaseResponse getExchangeRate() {
        return BaseResponse.res(ResponseStatus.SUCCESS,ResponseStatus.SUCCESS.getMessage(),exchangeService.getExchangeRate());
    }
}
