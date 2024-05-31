package com.hanaro.triptogether.dues.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hanaro.triptogether.common.firebase.FirebaseFCMService;
import com.hanaro.triptogether.common.response.BaseResponse;
import com.hanaro.triptogether.common.response.ResponseStatus;
import com.hanaro.triptogether.dues.dto.request.DuesAlarmRequestDto;
import com.hanaro.triptogether.dues.dto.request.DuesRuleRequestDto;
import com.hanaro.triptogether.dues.dto.response.DuesListResponseDto;
import com.hanaro.triptogether.dues.service.DuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dues")
public class DuesController {

    private final DuesService duesService;
    private final FirebaseFCMService firebaseFCMService;

    @PostMapping("/rule")
    public BaseResponse setDuesRule(@RequestBody DuesRuleRequestDto duesRuleRequestDto) {
        duesService.setDuesRule(duesRuleRequestDto);
        return BaseResponse.res(ResponseStatus.SUCCESS, ResponseStatus.SUCCESS.getMessage());
    }

    @GetMapping("/rule")
    public BaseResponse getDuesRule(@RequestBody Map<String, Long> teamIdx) {
        return BaseResponse.res(ResponseStatus.SUCCESS,ResponseStatus.SUCCESS.getMessage(),duesService.getDuesRule(teamIdx.get("teamIdx")));
    }

    @GetMapping("")
    public BaseResponse<DuesListResponseDto> getDuesList(@RequestParam("teamIdx") Long teamIdx,@RequestParam("accIdx") Long accIdx, @RequestParam("paid") boolean paid, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM") YearMonth date) {
        return BaseResponse.res(ResponseStatus.SUCCESS, ResponseStatus.SUCCESS.getMessage(), duesService.getDuesList(teamIdx,accIdx, date, paid));
    }


    @PostMapping("/request")
    public BaseResponse requestDuesToMember(@RequestBody DuesAlarmRequestDto duesAlarmRequestDto) throws IOException, FirebaseMessagingException {
        System.out.println("asdfasdf"+duesAlarmRequestDto.toString()+duesAlarmRequestDto.getMemberInfos().toString());
        return firebaseFCMService.notificationAlarm("회비 요청 알림",duesAlarmRequestDto.getDuesAmount().toString(),duesAlarmRequestDto);
    }


}
