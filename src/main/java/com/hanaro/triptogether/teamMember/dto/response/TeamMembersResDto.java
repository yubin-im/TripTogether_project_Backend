package com.hanaro.triptogether.teamMember.dto.response;

import com.hanaro.triptogether.enumeration.TeamMemberState;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamMembersResDto {
    private Long teamMemberIdx;
    private String memberName;
    private TeamMemberState teamMemberState;
    private Long memberIdx;
}
