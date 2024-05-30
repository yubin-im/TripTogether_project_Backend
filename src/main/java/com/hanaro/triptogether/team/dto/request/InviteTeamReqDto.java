package com.hanaro.triptogether.team.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InviteTeamReqDto {
    private Long memberIdx;
    private Long teamIdx;
}
