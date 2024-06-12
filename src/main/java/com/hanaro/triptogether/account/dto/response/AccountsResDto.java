package com.hanaro.triptogether.account.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AccountsResDto {
    private Long accIdx;
    private String accNumber;
    private String accName;
    private BigDecimal accBalance;
}
