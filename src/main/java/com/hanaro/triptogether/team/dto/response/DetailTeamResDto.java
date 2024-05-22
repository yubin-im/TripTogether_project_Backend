package com.hanaro.triptogether.team.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DetailTeamResDto {
    private Long teamIdx;
    private String teamNotice;
    private String teamName;
    private String accNumber;
    private BigDecimal accBalance;
}
