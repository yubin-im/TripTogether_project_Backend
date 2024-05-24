package com.hanaro.triptogether.team.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ManageTeamResDto {
    private String teamName;
    private String accNumber;
    private BigDecimal accBalance;
    private Boolean alarmStatus;
}
