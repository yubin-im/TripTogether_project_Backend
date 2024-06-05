package com.hanaro.triptogether.account.dto.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateAccBalanceReq {
    private Long depositAccIdx;  // 입금 계좌
    private Long withdrawAccIdx;  // 출금 계좌
    private BigDecimal amount;
    private String memo;
}
