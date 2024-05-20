package com.hanaro.triptogether.account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountsResDto {
    private Long accIdx;
    private String accNumber;
    private String accName;
}
