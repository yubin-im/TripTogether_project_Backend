package com.hanaro.triptogether.team.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailTeamReqDto {
    private Long teamIdx;
    private Long teamMemberIdx;
}
