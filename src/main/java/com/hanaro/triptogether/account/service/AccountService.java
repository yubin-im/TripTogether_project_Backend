package com.hanaro.triptogether.account.service;

import com.hanaro.triptogether.account.dto.request.UpdateAccBalanceReq;
import com.hanaro.triptogether.account.dto.response.AccountsResDto;
import com.hanaro.triptogether.account.dto.response.TeamServiceListResDto;

import java.util.List;

public interface AccountService {
    // 모임서비스 전체 조회
    List<TeamServiceListResDto> teamServiceList(Long memberIdx);

    // 전체 계좌 조회 (계좌 선택 기능)
    List<AccountsResDto> accounts(Long memberIdx);

    // 계좌 입출금
    void depositAcc(UpdateAccBalanceReq updateAccBalanceReq);

    // 계좌 출금
//    void withdrawAcc(UpdateAccBalanceReq updateAccBalanceReq);
}
