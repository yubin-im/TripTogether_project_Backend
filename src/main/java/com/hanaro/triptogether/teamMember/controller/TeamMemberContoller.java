package com.hanaro.triptogether.teamMember.controller;

import com.hanaro.triptogether.teamMember.dto.request.ChangeOwnerReqDto;
import com.hanaro.triptogether.teamMember.dto.response.TeamMembersResDto;
import com.hanaro.triptogether.teamMember.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TeamMemberContoller {
    private final TeamMemberService teamMemberService;

    // 모임원 전체 출력
    @PostMapping("/team")
    public List<TeamMembersResDto> teamMembers(@RequestBody Map<String, Long> teamIdxMap) {
        Long teamIdx = teamIdxMap.get("teamIdx");
        List<TeamMembersResDto> teamMembersResDtos = teamMemberService.teamMembers(teamIdx);

        return teamMembersResDtos;
    }

    // 총무 변경
    @PutMapping("/team/change-owner")
    public void changeOwner(@RequestBody ChangeOwnerReqDto changeOwnerReqDto) {
        teamMemberService.changeOwner(changeOwnerReqDto);
    }
}
