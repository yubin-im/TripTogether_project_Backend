package com.hanaro.triptogether.team.dto.request;

import lombok.Getter;

@Getter
public class UpdateTeamNoticeReq {
    private Long teamIdx;
    private String teamNotice;
}
