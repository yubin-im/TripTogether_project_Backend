package com.hanaro.triptogether.exchangeRate.controller;

import com.hanaro.triptogether.common.response.BaseResponse;
import com.hanaro.triptogether.common.response.ResponseStatus;
import com.hanaro.triptogether.exchangeRate.dto.request.ExchangeRateAlarmRequestDto;
import com.hanaro.triptogether.exchangeRate.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExchangeRateAlarmController {

    private final ExchangeService exchangeService;


    @PostMapping("/exchange-rate")
    public BaseResponse setExchangeRateAlarm(@RequestBody ExchangeRateAlarmRequestDto requestDto){
        exchangeService.setExchangeRateAlarm(requestDto);
        return BaseResponse.res(ResponseStatus.SUCCESS,ResponseStatus.SUCCESS.getMessage());
    }


//    @PostMapping("/notification")
//    public BaseResponse sendNotification(@RequestBody FcmSendDto fcmSendDto)throws IOException {
//        firebaseFCMService.sendMessageTo(fcmSendDto);
//        return BaseResponse.res(ResponseStatus.SUCCESS,ResponseStatus.SUCCESS.getMessage());
//    }


}
