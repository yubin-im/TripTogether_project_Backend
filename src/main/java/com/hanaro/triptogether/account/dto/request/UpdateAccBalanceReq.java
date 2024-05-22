package com.hanaro.triptogether.account.dto.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateAccBalanceReq {
    private Long accIdx;
    private BigDecimal amount;
    private String memo;
}
