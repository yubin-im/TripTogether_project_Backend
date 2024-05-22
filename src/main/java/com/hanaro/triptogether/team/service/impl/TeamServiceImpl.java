package com.hanaro.triptogether.team.service.impl;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.team.dto.request.AddTeamReqDto;
import com.hanaro.triptogether.team.dto.request.ExportTeamReqDto;
import com.hanaro.triptogether.team.dto.request.ManageTeamReqDto;
import com.hanaro.triptogether.team.dto.response.DetailTeamResDto;
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
}
