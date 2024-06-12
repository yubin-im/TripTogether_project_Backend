package com.hanaro.triptogether.teamMember.dto.request;

import lombok.Getter;

@Getter
public class ChangeOwnerReqDto {
    private Long teamIdx;
    private Long curOwnerIdx; // 기존 총무 team_member_idx, 모임원으로 변경
    private Long newOwnerIdx; // 새로운 총무가 될 team_member_idx
}
