package com.hanaro.triptogether.team.dto.response;

import com.hanaro.triptogether.enumeration.TeamMemberState;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DetailTeamResDto {
    private String teamNotice;
    private String teamName;
    private String accNumber;
    private BigDecimal accBalance;
    private TeamMemberState teamMemberState;
}
