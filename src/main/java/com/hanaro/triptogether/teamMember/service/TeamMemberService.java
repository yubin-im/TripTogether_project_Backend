package com.hanaro.triptogether.teamMember.service;

import com.hanaro.triptogether.teamMember.dto.response.TeamMembersResDto;

import java.util.List;

public interface TeamMemberService {
    // 모임원 전체 출력
    List<TeamMembersResDto> teamMembers(Long teamIdx);
}
