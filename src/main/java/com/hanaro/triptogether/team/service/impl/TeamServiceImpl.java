package com.hanaro.triptogether.team.service.impl;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.team.dto.request.AddTeamReqDto;
import com.hanaro.triptogether.team.dto.response.DetailTeamResDto;
import com.hanaro.triptogether.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final AccountRepository accountRepository;

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
}
