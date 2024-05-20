package com.hanaro.triptogether.account.service.impl;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
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
    public List<TeamServiceListResDto> TeamServiceList(Long memberIdx) {
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
}
