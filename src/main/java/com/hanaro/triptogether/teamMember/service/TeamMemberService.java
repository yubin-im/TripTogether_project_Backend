package com.hanaro.triptogether.teamMember.service;

import com.hanaro.triptogether.teamMember.dto.request.AcceptTeamMemberReqDto;
import com.hanaro.triptogether.teamMember.dto.request.ChangeOwnerReqDto;
import com.hanaro.triptogether.teamMember.dto.response.TeamMembersResDto;

import java.util.List;

public interface TeamMemberService {
    // 모임원 전체 출력
    List<TeamMembersResDto> teamMembers(Long teamIdx);

    // 총무 변경
    void changeOwner(ChangeOwnerReqDto changeOwnerReqDto);

    // 모임원 수락 (수락대기-> 모임원으로 상태 변경)
    void acceptTeamMember(AcceptTeamMemberReqDto acceptTeamMemberReqDto);

    // 모임원 전체 수락 (수락대기-> 모임원으로 상태 변경)
    void acceptTeamMembers(Long teamIdx);

    // 모임원 거절 (모임원 삭제)
    void rejectTeamMember(AcceptTeamMemberReqDto acceptTeamMemberReqDto);

    // 모임원 전체 거절 (모임원 삭제)
    void rejectTeamMembers(Long teamIdx);
}
