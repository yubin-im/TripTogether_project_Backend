package com.hanaro.triptogether.team.service;

import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.dto.request.*;
import com.hanaro.triptogether.team.dto.response.DetailTeamResDto;
import com.hanaro.triptogether.team.dto.response.InviteTeamResDto;
import com.hanaro.triptogether.team.dto.response.ManageTeamResDto;

public interface TeamService {
    // 모임서비스 가입
    void addTeam(AddTeamReqDto addTeamReqDto);

    // 모임서비스 상세
    DetailTeamResDto detailTeam(DetailTeamReqDto detailTeamReqDto);

    // 모임서비스 나가기 (전체 내보내기 후 모임 삭제)
    void exportTeam(ExportTeamReqDto exportTeamReqDto);

    // 모임서비스 관리 (설정)
    ManageTeamResDto manageTeam(ManageTeamReqDto manageTeamReqDto);

    // 공지 등록/수정
    void updateTeamNotice(UpdateTeamNoticeReq updateTeamNoticeReq);

    // 모임 초대하기 (초대링크 생성)
    String generateInviteLink(InviteTeamReqDto inviteTeamReqDto);

    // 모임에 초대받은 화면
    InviteTeamResDto inviteTeam(String inviter, Long teamNo);

    //모임 검색
    Team findTeamByTeamIdx(Long teamIdx);

    void updateTeamPreference(UpdateTeamPreferenceReqDto dto);
}
