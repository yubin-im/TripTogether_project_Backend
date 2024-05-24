package com.hanaro.triptogether.team.service.impl;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final AccountRepository accountRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberRepository memberRepository;

    // 모임서비스 가입
    @Transactional
    @Override
    public void addTeam(AddTeamReqDto addTeamReqDto) {
        Account account = accountRepository.findById(addTeamReqDto.getAccIdx()).orElse(null);

        Team team = Team.builder()
                .account(account)
                .teamType(addTeamReqDto.getTeamType())
                .teamName(addTeamReqDto.getTeamName())
                .preferenceType(addTeamReqDto.getPreferenceType())
                .createdAt(LocalDateTime.now())
                .createdBy(addTeamReqDto.getMemberIdx())
                .lastModifiedAt(LocalDateTime.now())
                .lastModifiedBy(addTeamReqDto.getMemberIdx())
                .build();

        teamRepository.save(team);
    }

    // 모임서비스 상세
    @Transactional
    @Override
    public DetailTeamResDto detailTeam(Long accIdx) {
        Account account = accountRepository.findById(accIdx).orElse(null);
        Team team = teamRepository.findTeamByAccount(account);

        DetailTeamResDto detailTeamResDto = DetailTeamResDto.builder()
                .teamIdx(team.getTeamIdx())
                .teamNotice(team.getTeamNotice())
                .teamName(team.getTeamName())
                .accNumber(account.getAccNumber())
                .accBalance(account.getAccBalance())
                .build();

        return detailTeamResDto;
    }

    // 모임서비스 나가기 (전체 내보내기 후 모임 삭제)
    @Transactional
    @Override
    public void exportTeam(ExportTeamReqDto exportTeamReqDto) {
        Team team = teamRepository.findById(exportTeamReqDto.getTeamIdx()).orElse(null);
        List<TeamMember> teamMembers = teamMemberRepository.findTeamMembersByTeam(team);

        // 전체 내보내기
        for(int i = 0; i < teamMembers.size(); i++) {
            teamMembers.get(i).delete(LocalDateTime.now(), exportTeamReqDto.getMemberIdx());
            teamMemberRepository.save(teamMembers.get(i));
        }

        // 모임 삭제
        team.delete(LocalDateTime.now(), exportTeamReqDto.getMemberIdx());
        teamRepository.save(team);
    }

    // 모임서비스 관리 (설정)
    @Transactional
    @Override
    public ManageTeamResDto manageTeam(ManageTeamReqDto manageTeamReqDto) {
        Team team = teamRepository.findById(manageTeamReqDto.getTeamIdx()).orElse(null);
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
        Team team = teamRepository.findById(updateTeamNoticeReq.getTeamIdx()).orElse(null);

        team.updateTeamNotice(updateTeamNoticeReq.getTeamNotice());
        teamRepository.save(team);
    }

    // 모임 초대하기 (초대링크 생성)
    @Transactional
    @Override
    public String generateInviteLink(InviteTeamReqDto inviteTeamReqDto) {
        Team team = teamRepository.findById(inviteTeamReqDto.getTeamIdx()).orElse(null);
        Member member = memberRepository.findById(inviteTeamReqDto.getMemberIdx()).orElse(null);

        String inviter = member.getMemberName();
        Long teamIdx = team.getTeamIdx();
        String inviteUrl = "http://localhost:8080/invite?inviter=" + inviter + "&teamNo=" + teamIdx;

        return "[하나은행]\n" + inviter + "님이 " + team.getTeamName() + "에 초대했어요.\n" + inviteUrl;
    }

    // 모임에 초대받은 화면
    @Transactional
    @Override
    public InviteTeamResDto inviteTeam(String inviter, Long teamNo) {
        Team team = teamRepository.findById(teamNo).orElse(null);

        InviteTeamResDto inviteTeamResDto = InviteTeamResDto.builder()
                .inviter(inviter)
                .teamName(team.getTeamName())
                .teamNo(teamNo)
                .build();

        return inviteTeamResDto;
    }
}
