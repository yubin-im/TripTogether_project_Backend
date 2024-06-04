package com.hanaro.triptogether.team.dto.request;

import lombok.Getter;

@Getter
public class UpdateTeamPreferenceReqDto {
    private Long teamIdx;
    private Long tripIdx;
    private Long memberIdx;
}
