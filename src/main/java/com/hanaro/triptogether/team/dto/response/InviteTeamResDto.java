package com.hanaro.triptogether.team.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InviteTeamResDto {
    private String inviter;
    private String teamName;
    private Long teamNo;
}
