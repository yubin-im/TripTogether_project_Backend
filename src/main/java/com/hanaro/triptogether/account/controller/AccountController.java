package com.hanaro.triptogether.account.controller;

import com.hanaro.triptogether.account.dto.request.UpdateAccBalanceReq;
import com.hanaro.triptogether.account.dto.response.AccountsResDto;
import com.hanaro.triptogether.account.dto.response.TeamServiceListResDto;
import com.hanaro.triptogether.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        return accountService.teamServiceList(memberIdx);
    }

    // 전체 계좌 조회 (계좌 선택 기능)
    @PostMapping("/accounts")
    public List<AccountsResDto> accounts(@RequestBody Map<String, Long> memberIdxMap) {
        Long memberIdx = memberIdxMap.get("memberIdx");
        return accountService.accounts(memberIdx);
    }

    // 계좌 입금
    @PutMapping("/account/deposit")
    public void depositAcc(@RequestBody UpdateAccBalanceReq updateAccBalanceReq) {
        accountService.depositAcc(updateAccBalanceReq);
    }

    // 계좌 출금
    @PutMapping("/account/withdraw")
    public void withdrawAcc(@RequestBody UpdateAccBalanceReq updateAccBalanceReq) {
        accountService.withdrawAcc(updateAccBalanceReq);
    }
}
