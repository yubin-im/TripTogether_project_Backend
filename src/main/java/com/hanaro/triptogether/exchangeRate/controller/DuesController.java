package com.hanaro.triptogether.exchangeRate.controller;

import com.hanaro.triptogether.exchangeRate.dto.request.DuesRuleRequestDto;
import com.hanaro.triptogether.exchangeRate.service.DuesService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DuesController {

    private final DuesService duesService;

    @PostMapping("/dues")
    public ResponseEntity setDuesRule(@RequestBody DuesRuleRequestDto duesRuleRequestDto) {

    }



}
