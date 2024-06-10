package com.hanaro.triptogether.teamMember.service;

import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.dto.request.*;
import com.hanaro.triptogether.teamMember.dto.response.TeamMembersResDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TeamMemberService {
    // 모임원 전체 출력
    List<TeamMembersResDto> teamMembers(Long teamIdx);

    // 총무 변경
    void changeOwner(ChangeOwnerReqDto changeOwnerReqDto);

    // 모임원 수락 (수락대기-> 모임원으로 상태 변경)
    void acceptTeamMember(AcceptTeamMemberReqDto acceptTeamMemberReqDto) throws IOException;

    // 모임원 전체 수락 (수락대기-> 모임원으로 상태 변경)
    void acceptTeamMembers(AcceptTeamMembersReqDto acceptTeamMembersReqDto);

    // 모임원 거절 (수락대기-> 모임원 삭제)
    // 모임원 내보내기 (모임원-> 모임원 삭제)
    void rejectTeamMember(AcceptTeamMemberReqDto acceptTeamMemberReqDto);

    // 모임원 전체 거절 (수락대기-> 모임원 삭제)
    void rejectTeamMembers(RejectTeamMembersReqDto rejectTeamMembersReqDto);

    // 모임원 전체 내보내기 (모임원-> 모임원 삭제)
    void exportTeamMembers(RejectTeamMembersReqDto rejectTeamMembersReqDto);

    //팀 멤버 idx로 팀 멤버 검색
    TeamMember findTeamMemberByTeamMemberIdx(Long team_member_idx);

    //멤버id로 팀 검색
    List<TeamMember> findTeamMemberByMemberIdx(Long member_idx);

    // 모임원인지 확인
    TeamMember checkIsMyTeam(Team dtoTeam, List<TeamMember> teamMembers);

    //유효한 팀원인지 확인
    void validateTeamMemberState(TeamMember teamMember);

    // 모임 가입
    void joinTeamMember(JoinTeamMemberReq joinTeamMemberReq) throws IOException;

    // 팀멤버idx로 팀멤버 검색
    TeamMember checkIsMyTeamByTeamMemberIdx(Long team_member_idx);

    //memberId와 teamId로 팀멤버 검색
    TeamMember findTeamMemberByMemberIdxAndTeamIdx(Long memberId, Long teamId);
}
