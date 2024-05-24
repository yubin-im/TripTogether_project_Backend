package com.hanaro.triptogether.account.service.impl;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.account.dto.request.UpdateAccBalanceReq;
import com.hanaro.triptogether.account.dto.response.AccountsResDto;
import com.hanaro.triptogether.account.dto.response.TeamServiceListResDto;
import com.hanaro.triptogether.account.service.AccountService;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    // 모임서비스 전체 조회
    @Transactional
    @Override
    public List<TeamServiceListResDto> teamServiceList(Long memberIdx) {
        List<TeamServiceListResDto> teamServiceListResDtos = new ArrayList<>();

        Member member = memberRepository.findById(memberIdx).orElse(null);
        List<Account> accounts = accountRepository.findAccountsByMember(member);

        for(int i = 0; i < accounts.size(); i++) {
            TeamServiceListResDto teamServiceListResDto = TeamServiceListResDto.builder()
                    .accIdx(accounts.get(i).getAccIdx())
                    .accNumber(accounts.get(i).getAccNumber())
                    .accBalance(accounts.get(i).getAccBalance())
                    .teamName(teamRepository.findTeamByAccount(accounts.get(i)).getTeamName())
                    .build();

            teamServiceListResDtos.add(teamServiceListResDto);
        }

        return teamServiceListResDtos;
    }

    // 전체 계좌 조회 (계좌 선택 기능)
    @Transactional
    @Override
    public List<AccountsResDto> accounts(Long memberIdx) {
        List<AccountsResDto> accountsResDtos = new ArrayList<>();

        Member member = memberRepository.findById(memberIdx).orElse(null);
        List<Account> accounts = accountRepository.findAccountsByMember(member);

        for(int i = 0; i < accounts.size(); i++) {
            AccountsResDto accountsResDto = AccountsResDto.builder()
                    .accIdx(accounts.get(i).getAccIdx())
                    .accNumber(accounts.get(i).getAccNumber())
                    .accName(accounts.get(i).getAccName())
                    .build();

            accountsResDtos.add(accountsResDto);
        }

        return accountsResDtos;
    }

    // 계좌 입금
    @Transactional
    @Override
    public void depositAcc(UpdateAccBalanceReq updateAccBalanceReq) {
        Account account = accountRepository.findById(updateAccBalanceReq.getAccIdx()).orElse(null);

        account.updateAccBalance(account.getAccBalance().add(updateAccBalanceReq.getAmount()));
        accountRepository.save(account);
    }

    // 계좌 출금
    @Transactional
    @Override
    public void withdrawAcc(UpdateAccBalanceReq updateAccBalanceReq) {
        Account account = accountRepository.findById(updateAccBalanceReq.getAccIdx()).orElse(null);

        account.updateAccBalance(account.getAccBalance().subtract(updateAccBalanceReq.getAmount()));
        accountRepository.save(account);
    }
}
