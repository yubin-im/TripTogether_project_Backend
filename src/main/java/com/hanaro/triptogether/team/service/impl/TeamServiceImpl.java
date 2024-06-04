package com.hanaro.triptogether.team.service.impl;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.enumeration.TeamMemberState;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.team.dto.request.*;
import com.hanaro.triptogether.team.dto.response.DetailTeamResDto;
import com.hanaro.triptogether.team.dto.response.InviteTeamResDto;
import com.hanaro.triptogether.team.dto.response.ManageTeamResDto;
import com.hanaro.triptogether.team.service.TeamService;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.trip.domain.TripRepository;
import com.hanaro.triptogether.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TripRepository tripRepository;
    private final TeamRepository teamRepository;
    private final AccountRepository accountRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    // 모임서비스 가입
    @Transactional
    @Override
    public void addTeam(AddTeamReqDto addTeamReqDto) {
        Account account = accountRepository.findById(addTeamReqDto.getAccIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.ACCOUNT_NOT_FOUND));
        Member member = memberRepository.findById(addTeamReqDto.getMemberIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));

        Team team = Team.builder()
                .account(account)
                .teamType(addTeamReqDto.getTeamType())
                .teamName(addTeamReqDto.getTeamName())
                .preferenceType(addTeamReqDto.getPreferenceType())
                .createdAt(LocalDateTime.now())
                .createdBy(memberRepository.findById(addTeamReqDto.getMemberIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_FOUND)))
                .build();

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .teamMemberState(TeamMemberState.총무)
                .createdAt(LocalDateTime.now())
                .build();

        teamRepository.save(team);
        teamMemberRepository.save(teamMember);
    }

    // 모임서비스 상세
    @Transactional
    @Override
    public DetailTeamResDto detailTeam(DetailTeamReqDto detailTeamReqDto) {
        Team team = teamRepository.findById(detailTeamReqDto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        TeamMember teamMember = teamMemberRepository.findById(detailTeamReqDto.getTeamMemberIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_MEMBER_NOT_FOUND));

        DetailTeamResDto detailTeamResDto = DetailTeamResDto.builder()
                .teamNotice(team.getTeamNotice())
                .teamName(team.getTeamName())
                .accNumber(team.getAccount().getAccNumber())
                .accBalance(team.getAccount().getAccBalance())
                .teamMemberState(teamMember.getTeamMemberState())
                .build();

        return detailTeamResDto;
    }

    // 모임서비스 나가기 (전체 내보내기 후 모임 삭제)
    @Transactional
    @Override
    public void exportTeam(ExportTeamReqDto exportTeamReqDto) {
        Team team = teamRepository.findById(exportTeamReqDto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        // 전체 내보내기
        for(int i = 0; i < teamMembers.size(); i++) {
            teamMembers.get(i).updateTeamMemberState(TeamMemberState.거절);
            teamMembers.get(i).delete(LocalDateTime.now(), exportTeamReqDto.getMemberIdx());
            teamMemberRepository.save(teamMembers.get(i));
        }

        // 모임 삭제
        team.delete(LocalDateTime.now(), memberRepository.findById(exportTeamReqDto.getMemberIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_FOUND)));
        teamRepository.save(team);
    }

    // 모임서비스 관리 (설정)
    @Transactional
    @Override
    public ManageTeamResDto manageTeam(ManageTeamReqDto manageTeamReqDto) {
        Team team = teamRepository.findById(manageTeamReqDto.getTeamIdx()).orElseThrow(()->new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        Account account = team.getAccount();
        Member member = account.getMember();

        ManageTeamResDto manageTeamResDto = ManageTeamResDto.builder()
                .teamName(team.getTeamName())
                .accNumber(account.getAccNumber())
                .accBalance(account.getAccBalance())
                .alarmStatus(member.getAlarmStatus())
                .build();

        return manageTeamResDto;
    }

    // 공지 등록/수정
    @Transactional
    @Override
    public void updateTeamNotice(UpdateTeamNoticeReq updateTeamNoticeReq) {
        Team team = teamRepository.findById(updateTeamNoticeReq.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));

        team.updateTeamNotice(updateTeamNoticeReq.getTeamNotice());
        teamRepository.save(team);
    }

    // 모임 초대하기 (초대링크 생성)
    @Transactional
    @Override
    public String generateInviteLink(InviteTeamReqDto inviteTeamReqDto) {
        Team team = teamRepository.findById(inviteTeamReqDto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        Member member = memberRepository.findById(inviteTeamReqDto.getMemberIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));

        String inviter = member.getMemberName();
        Long teamIdx = team.getTeamIdx();
        String inviteUrl = "http://localhost:8080/invite?inviter=" + inviter + "&teamNo=" + teamIdx;

        return "[하나은행]\n" + inviter + "님이 " + team.getTeamName() + "에 초대했어요.\n" + inviteUrl;
    }

    // 모임에 초대받은 화면
    @Transactional
    @Override
    public InviteTeamResDto inviteTeam(String inviter, Long teamNo) {
        Team team = teamRepository.findById(teamNo).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));

        InviteTeamResDto inviteTeamResDto = InviteTeamResDto.builder()
                .inviter(inviter)
                .teamName(team.getTeamName())
                .teamNo(teamNo)
                .build();

        return inviteTeamResDto;
    }

    @Override
    public Team findTeamByTeamIdx(Long teamIdx) {
        return teamRepository.findById(teamIdx).orElseThrow(()->new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
    }

    @Override
    public void updateTeamPreference(UpdateTeamPreferenceReqDto dto) {
        Team team = teamRepository.findById(dto.getTeamIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TEAM_NOT_FOUND));
        Member member = memberRepository.findById(dto.getMemberIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));
        Trip trip =null;
        if(dto.getTripIdx() != null) {
            trip = tripRepository.findById(dto.getTripIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_NOT_FOUND));
            if(!trip.getTeam().equals(team)){
                throw new ApiException(ExceptionEnum.TEAM_AND_TRIP_NOT_MATCH);
            }
        }
        team.updatePreferTrip(trip, member);
        teamRepository.save(team);
    }
}
