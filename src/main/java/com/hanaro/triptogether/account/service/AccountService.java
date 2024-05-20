package com.hanaro.triptogether.account.service;

import com.hanaro.triptogether.account.dto.response.AccountListResDto;

import java.util.List;

public interface AccountService {
    // 모임서비스 전체 조회
    List<AccountListResDto> accountList(Long memberIdx);
}
