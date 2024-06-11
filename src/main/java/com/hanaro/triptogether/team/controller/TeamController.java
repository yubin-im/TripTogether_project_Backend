package com.hanaro.triptogether.team.controller;

import com.hanaro.triptogether.team.dto.request.*;
import com.hanaro.triptogether.team.dto.response.AddTeamResDto;
import com.hanaro.triptogether.team.dto.response.DetailTeamResDto;
import com.hanaro.triptogether.team.dto.response.InviteTeamResDto;
import com.hanaro.triptogether.team.dto.response.ManageTeamResDto;
import com.hanaro.triptogether.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    // 모임서비스 가입
    @PostMapping("/account/add")
    public AddTeamResDto addTeam(@RequestBody AddTeamReqDto addTeamReqDto) {
        return teamService.addTeam(addTeamReqDto);
    }

    // 모임서비스 상세
    @PostMapping("/account/detail")
    public DetailTeamResDto detailTeam(@RequestBody DetailTeamReqDto detailTeamReqDto) {
        return teamService.detailTeam(detailTeamReqDto);
    }

    // 모임서비스 나가기 (전체 내보내기 후 모임 삭제)
    @PutMapping("/team/export-team")
    public void exportTeam(@RequestBody ExportTeamReqDto exportTeamReqDto) {
        teamService.exportTeam(exportTeamReqDto);
    }

    // 모임서비스 관리 (설정)
    @PostMapping("/account/set")
    public ManageTeamResDto manageTeam(@RequestBody ManageTeamReqDto manageTeamReqDto) {
        return teamService.manageTeam(manageTeamReqDto);
    }

    // 공지 등록/수정
    @PutMapping("/account/notice")
    public void updateTeamNotice(@RequestBody UpdateTeamNoticeReq updateTeamNoticeReq) {
        teamService.updateTeamNotice(updateTeamNoticeReq);
    }

    // 모임 초대하기 (초대링크 생성)
    @PostMapping("/team/invite-team")
    public String generateInviteLink(@RequestBody InviteTeamReqDto inviteTeamReqDto) {
        return teamService.generateInviteLink(inviteTeamReqDto);
    }

    // 모임에 초대받은 화면
    @GetMapping("/invite")
    public InviteTeamResDto inviteTeam(@RequestParam String inviter, @RequestParam Long teamNo) {
        return teamService.inviteTeam(inviter, teamNo);
    }

    //여행 즐겨찾기
    @PutMapping("/team/preference")
    public void updateTeamPreference(@RequestBody UpdateTeamPreferenceReqDto dto) {
        teamService.updateTeamPreference(dto);
    }
}
