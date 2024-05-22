package com.hanaro.triptogether.team.controller;

import com.hanaro.triptogether.team.dto.request.AddTeamReqDto;
import com.hanaro.triptogether.team.dto.request.ExportTeamReqDto;
import com.hanaro.triptogether.team.dto.request.ManageTeamReqDto;
import com.hanaro.triptogether.team.dto.response.DetailTeamResDto;
import com.hanaro.triptogether.team.dto.response.ManageTeamResDto;
import com.hanaro.triptogether.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    // 모임서비스 가입
    @PostMapping("/account/add")
    public void addTeam(@RequestBody AddTeamReqDto addTeamReqDto) {
        teamService.addTeam(addTeamReqDto);
    }

    // 모임서비스 상세
    @PostMapping("/account/detail")
    public DetailTeamResDto detailTeam(@RequestBody Map<String, Long> accIdxMap) {
        Long accIdx = accIdxMap.get("accIdx");
        DetailTeamResDto detailTeamResDto = teamService.detailTeam(accIdx);

        return detailTeamResDto;
    }

    // 모임서비스 나가기 (전체 내보내기 후 모임 삭제)
    @PutMapping("/team/export-team")
    public void exportTeam(@RequestBody ExportTeamReqDto exportTeamReqDto) {
        teamService.exportTeam(exportTeamReqDto);
    }

    // 모임서비스 관리 (설정)
    @PostMapping("/account/set")
    public ManageTeamResDto manageTeam(@RequestBody ManageTeamReqDto manageTeamReqDto) {
        ManageTeamResDto manageTeamResDto = teamService.manageTeam(manageTeamReqDto);

        return manageTeamResDto;
    }
}
