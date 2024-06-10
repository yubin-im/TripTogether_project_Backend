package com.hanaro.triptogether.teamMember.controller;

import com.hanaro.triptogether.common.firebase.FirebaseFCMService;
import com.hanaro.triptogether.teamMember.dto.request.*;
import com.hanaro.triptogether.teamMember.dto.response.TeamMembersResDto;
import com.hanaro.triptogether.teamMember.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TeamMemberContoller {
    private final TeamMemberService teamMemberService;
    private final FirebaseFCMService firebaseFCMService;

    // 모임원 전체 출력
    @PostMapping("/team")
    public List<TeamMembersResDto> teamMembers(@RequestBody Map<String, Long> teamIdxMap) {
        Long teamIdx = teamIdxMap.get("teamIdx");
        return teamMemberService.teamMembers(teamIdx);
    }

    // 총무 변경
    @PutMapping("/team/change-owner")
    public void changeOwner(@RequestBody ChangeOwnerReqDto changeOwnerReqDto) {
        teamMemberService.changeOwner(changeOwnerReqDto);
    }

    // 모임원 수락 (수락대기-> 모임원으로 상태 변경)
    @PutMapping("/team/accept-one")
    public void acceptTeamMember(@RequestBody AcceptTeamMemberReqDto acceptTeamMemberReqDto) throws IOException {
        teamMemberService.acceptTeamMember(acceptTeamMemberReqDto);

    }

    // 모임원 전체 수락 (수락대기-> 모임원으로 상태 변경)
    @PutMapping("/team/accept-all")
    public void acceptTeamMembers(@RequestBody AcceptTeamMembersReqDto acceptTeamMembersReqDto) {
        teamMemberService.acceptTeamMembers(acceptTeamMembersReqDto);
    }

    // 모임원 거절 (수락대기-> 모임원 삭제)
    @PutMapping("/team/reject-one")
    public void rejectTeamMember(@RequestBody AcceptTeamMemberReqDto acceptTeamMemberReqDto) throws IOException {
        teamMemberService.rejectTeamMember(acceptTeamMemberReqDto);
    }

    // 모임원 전체 거절 (수락대기-> 모임원 삭제)
    @PutMapping("/team/reject-all")
    public void rejectTeamMembers(@RequestBody RejectTeamMembersReqDto rejectTeamMembersReqDto) {
        teamMemberService.rejectTeamMembers(rejectTeamMembersReqDto);
    }

    // 모임원 내보내기 (모임원-> 모임원 삭제)
    @PutMapping("/team/export-member")
    public void exportTeamMember(@RequestBody AcceptTeamMemberReqDto acceptTeamMemberReqDto) throws IOException {
        teamMemberService.rejectTeamMember(acceptTeamMemberReqDto);
    }

    // 모임원 전체 내보내기 (모임원-> 모임원 삭제)
    @PutMapping("/team/export-members")
    public void exportTeamMembers(@RequestBody RejectTeamMembersReqDto rejectTeamMembersReqDt) {
        teamMemberService.exportTeamMembers(rejectTeamMembersReqDt);
    }

    // 모임 가입
    @PostMapping("/team/join")
    public void joinTeamMember(@RequestBody JoinTeamMemberReq joinTeamMemberReq) throws IOException {
        teamMemberService.joinTeamMember(joinTeamMemberReq);
    }
}
