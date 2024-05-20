package com.hanaro.triptogether.account.controller;

import com.hanaro.triptogether.account.dto.response.AccountsResDto;
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
    public List<TeamServiceListResDto> teamServiceList(@RequestBody Map<String, Long> memberIdxMap) {
        Long memberIdx = memberIdxMap.get("memberIdx");
        List<TeamServiceListResDto> teamServiceListResDtos = accountService.teamServiceList(memberIdx);

        return teamServiceListResDtos;
    }

    // 전체 계좌 조회 (계좌 선택 기능)
    @PostMapping("/accounts")
    public List<AccountsResDto> accounts(@RequestBody Map<String, Long> memberIdxMap) {
        Long memberIdx = memberIdxMap.get("memberIdx");
        List<AccountsResDto> accountsResDtos = accountService.accounts(memberIdx);

        return accountsResDtos;
    }
}
