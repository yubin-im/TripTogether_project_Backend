package com.hanaro.triptogether.team.service;

import com.hanaro.triptogether.team.dto.request.AddTeamReqDto;
import com.hanaro.triptogether.team.dto.request.ExportTeamReqDto;
import com.hanaro.triptogether.team.dto.response.DetailTeamResDto;

public interface TeamService {
    // 모임서비스 가입
    void addTeam(AddTeamReqDto addTeamReqDto);

    // 모임서비스 상세
    DetailTeamResDto detailTeam(Long accIdx);

    // 모임서비스 나가기 (전체 내보내기 후 모임 삭제)
    void exportTeam(ExportTeamReqDto exportTeamReqDto);
}
