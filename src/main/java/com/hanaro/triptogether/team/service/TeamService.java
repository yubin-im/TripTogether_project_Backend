package com.hanaro.triptogether.team.service;

import com.hanaro.triptogether.team.dto.request.AddTeamReqDto;
import com.hanaro.triptogether.team.dto.request.ExportTeamReqDto;
import com.hanaro.triptogether.team.dto.request.ManageTeamReqDto;
import com.hanaro.triptogether.team.dto.request.UpdateTeamNoticeReq;
import com.hanaro.triptogether.team.dto.response.DetailTeamResDto;
import com.hanaro.triptogether.team.dto.response.ManageTeamResDto;

public interface TeamService {
    // 모임서비스 가입
    void addTeam(AddTeamReqDto addTeamReqDto);

    // 모임서비스 상세
    DetailTeamResDto detailTeam(Long accIdx);

    // 모임서비스 나가기 (전체 내보내기 후 모임 삭제)
    void exportTeam(ExportTeamReqDto exportTeamReqDto);

    // 모임서비스 관리 (설정)
    ManageTeamResDto manageTeam(ManageTeamReqDto manageTeamReqDto);

    // 공지 등록/수정
    void updateTeamNotice(UpdateTeamNoticeReq updateTeamNoticeReq);
}
