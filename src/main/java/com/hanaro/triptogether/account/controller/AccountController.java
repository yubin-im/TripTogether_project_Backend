package com.hanaro.triptogether.account.controller;

import com.hanaro.triptogether.account.dto.response.TeamServiceListResDto;
import com.hanaro.triptogether.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    // 모임서비스 전체 조회
    @PostMapping("/account")
    public List<TeamServiceListResDto> accountList(@RequestBody Map<String, Long> memberIdxMap) {
        Long memberIdx = memberIdxMap.get("memberIdx");
        List<TeamServiceListResDto> teamServiceListResDtos = accountService.TeamServiceList(memberIdx);

        return teamServiceListResDtos;
    }
}
