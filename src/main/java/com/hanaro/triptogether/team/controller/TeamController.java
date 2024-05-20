package com.hanaro.triptogether.team.controller;

import com.hanaro.triptogether.team.dto.request.AddTeamReqDto;
import com.hanaro.triptogether.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    // 모임서비스 가입
    @PostMapping("/account/add")
    public void addTeam(@RequestBody AddTeamReqDto addTeamReqDto) {
        teamService.addTeam(addTeamReqDto);
    }
}
