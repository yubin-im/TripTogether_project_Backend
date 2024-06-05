package com.hanaro.triptogether.account.service.impl;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.account.dto.request.UpdateAccBalanceReq;
import com.hanaro.triptogether.account.dto.response.AccountsResDto;
import com.hanaro.triptogether.account.dto.response.TeamServiceListResDto;
import com.hanaro.triptogether.account.service.AccountService;
import com.hanaro.triptogether.enumeration.TeamMemberState;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    // 모임서비스 전체 조회
    @Transactional
    @Override
    public List<TeamServiceListResDto> teamServiceList(Long memberIdx) {
        List<TeamServiceListResDto> teamServiceListResDtos = new ArrayList<>();

        List<Team> teams = teamRepository.findTeamsByMemberIdx(memberIdx);

        for (Team team : teams) {
            if (team.getDeletedAt() != null || team.getDeletedBy()!=null ) continue;
            Optional<TeamMember> teamMember = teamMemberRepository.findTeamMemberByMember_MemberIdxAndTeam_TeamIdx(memberIdx, team.getTeamIdx());
            if (teamMember.isPresent() && (teamMember.get().getTeamMemberState() == TeamMemberState.총무 || teamMember.get().getTeamMemberState() == TeamMemberState.모임원)) {
                Long teamMemberIdx = teamMember.get().getTeamMemberIdx();
                TeamServiceListResDto teamServiceListResDto = TeamServiceListResDto.builder()
                        .accIdx(team.getAccount().getAccIdx())
                        .accNumber(team.getAccount().getAccNumber())
                        .accBalance(team.getAccount().getAccBalance())
                        .teamName(team.getTeamName())
                        .teamIdx(team.getTeamIdx())
                        .teamMemberIdx(teamMemberIdx)
                        .teamMemberState(teamMember.get().getTeamMemberState().name())
                        .build();
                teamServiceListResDtos.add(teamServiceListResDto);
            }
        }

        return teamServiceListResDtos;
    }

    // 전체 계좌 조회 (계좌 선택 기능)
    @Transactional
    @Override
    public List<AccountsResDto> accounts(Long memberIdx) {
        List<AccountsResDto> accountsResDtos = new ArrayList<>();

        Member member = memberRepository.findById(memberIdx).orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));
        List<Account> accounts = accountRepository.findAccountsByMember(member);

        for(int i = 0; i < accounts.size(); i++) {
            AccountsResDto accountsResDto = AccountsResDto.builder()
                    .accIdx(accounts.get(i).getAccIdx())
                    .accNumber(accounts.get(i).getAccNumber())
                    .accName(accounts.get(i).getAccName())
                    .accBalance(accounts.get(i).getAccBalance())
                    .build();

            accountsResDtos.add(accountsResDto);
        }

        return accountsResDtos;
    }

    // 계좌 입금
    @Transactional
    @Override
    public void depositAcc(UpdateAccBalanceReq updateAccBalanceReq) {
        Account account = accountRepository.findById(updateAccBalanceReq.getAccIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.ACCOUNT_NOT_FOUND));

        account.updateAccBalance(account.getAccBalance().add(updateAccBalanceReq.getAmount()));
        account.updateModifiedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    // 계좌 출금
    @Transactional
    @Override
    public void withdrawAcc(UpdateAccBalanceReq updateAccBalanceReq) {
        Account account = accountRepository.findById(updateAccBalanceReq.getAccIdx()).orElseThrow(() -> new ApiException(ExceptionEnum.ACCOUNT_NOT_FOUND));

        account.updateAccBalance(account.getAccBalance().subtract(updateAccBalanceReq.getAmount()));
        account.updateModifiedAt(LocalDateTime.now());
        accountRepository.save(account);
    }
}
