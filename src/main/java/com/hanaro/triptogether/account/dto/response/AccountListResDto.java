package com.hanaro.triptogether.account.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AccountListResDto {
    private Long accIdx;
    private String accNumber;
    private BigDecimal accBalance;
    private String teamName;
}
