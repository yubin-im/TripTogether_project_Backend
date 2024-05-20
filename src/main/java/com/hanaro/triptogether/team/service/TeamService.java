package com.hanaro.triptogether.team.service;

import com.hanaro.triptogether.team.dto.request.AddTeamReqDto;

public interface TeamService {
    // 모임서비스 가입
    void addTeam(AddTeamReqDto addTeamReqDto);
}
