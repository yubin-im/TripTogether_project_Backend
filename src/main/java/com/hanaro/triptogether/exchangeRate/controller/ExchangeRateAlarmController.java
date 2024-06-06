package com.hanaro.triptogether.exchangeRate.controller;

import com.hanaro.triptogether.common.response.BaseResponse;
import com.hanaro.triptogether.common.response.ResponseStatus;
import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateAlarmRequestDto;
import com.hanaro.triptogether.exchangeRate.dto.response.ExchangeRateAlarmResponseDto;
import com.hanaro.triptogether.exchangeRate.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExchangeRateAlarmController {

    private final ExchangeService exchangeService;


    @PostMapping("/exchange-rate")
    public BaseResponse setExchangeRateAlarm(@RequestBody ExchangeRateAlarmRequestDto requestDto){
        exchangeService.setExchangeRateAlarm(requestDto);
        return BaseResponse.res(ResponseStatus.SUCCESS,ResponseStatus.SUCCESS.getMessage());
    }

    @GetMapping("/exchange-rate/{memberIdx}")
    public BaseResponse<List<ExchangeRateAlarmResponseDto>> getExchangeRateAlarmList(@PathVariable("memberIdx") Long memberIdx){
        return BaseResponse.res(ResponseStatus.SUCCESS,ResponseStatus.SUCCESS.getMessage(),exchangeService.getExchangeRateAlarmList(memberIdx));
    }


}
