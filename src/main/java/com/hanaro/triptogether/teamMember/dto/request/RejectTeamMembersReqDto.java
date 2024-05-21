package com.hanaro.triptogether.teamMember.dto.request;

import lombok.Getter;

@Getter
public class RejectTeamMembersReqDto {
    private Long teamIdx;
    private Long memberIdx;
}
