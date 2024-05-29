package com.hanaro.triptogether;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.account.dto.response.AccountsResDto;
import com.hanaro.triptogether.account.dto.response.TeamServiceListResDto;
import com.hanaro.triptogether.account.service.impl.AccountServiceImpl;
import com.hanaro.triptogether.enumeration.PreferenceType;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AccountServiceTests extends TriptogetherApplicationTests {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    AccountServiceImpl accountService;

    private Member member;
    private Long memberIdx = 1L;

    private Account account1;
    private Account account2;
    private Long accIdx1 = 1L;
    private Long accIdx2 = 2L;

    private Team team1;
    private Team team2;
    private Long teamIdx1 = 1L;
    private Long teamIdx2 = 2L;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .memberIdx(memberIdx)
                .memberId("testId")
                .memberPw("testPw")
                .alarmStatus(true)
                .memberLoginPw("123456")
                .memberName("memberName")
                .createdAt(LocalDateTime.now())
                .build();

        account1 = Account.builder()
                .accIdx(accIdx1)
                .member(member)
                .accNumber("12345")
                .accBalance(BigDecimal.valueOf(1000.00))
                .cardStatus(false)
                .accName("Acc Name1")
                .createdAt(LocalDateTime.now())
                .build();

        account2 = Account.builder()
                .accIdx(accIdx2)
                .member(member)
                .accNumber("67890")
                .accBalance(BigDecimal.valueOf(2000.00))
                .cardStatus(false)
                .accName("Acc Name2")
                .createdAt(LocalDateTime.now())
                .build();

        team1 = Team.builder()
                .teamIdx(teamIdx1)
                .account(account1)
                .teamName("Team A")
                .preferenceType(PreferenceType.해외)
                .teamNotice("Team Notice1")
                .createdAt(LocalDateTime.now())
                .build();

        team2 = Team.builder()
                .teamIdx(teamIdx2)
                .account(account2)
                .teamName("Team B")
                .preferenceType(PreferenceType.국내)
                .teamNotice("Team Notice2")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("모임서비스 전체 조회 테스트")
    void testTeamServiceList() {
        // Given
        when(memberRepository.findById(memberIdx)).thenReturn(Optional.of(member));
        when(accountRepository.findAccountsByMember(any(Member.class))).thenReturn(Arrays.asList(account1, account2));
        when(teamRepository.findTeamByAccount(account1)).thenReturn(team1);
        when(teamRepository.findTeamByAccount(account2)).thenReturn(team2);

        // When
        List<TeamServiceListResDto> result = accountService.teamServiceList(memberIdx);

        // Then
        assertEquals(2, result.size());

        TeamServiceListResDto dto1 = result.get(0);
        assertEquals(account1.getAccIdx(), dto1.getAccIdx());
        assertEquals(account1.getAccNumber(), dto1.getAccNumber());
        assertEquals(account1.getAccBalance(), dto1.getAccBalance());
        assertEquals(team1.getTeamName(), dto1.getTeamName());

        TeamServiceListResDto dto2 = result.get(1);
        assertEquals(account2.getAccIdx(), dto2.getAccIdx());
        assertEquals(account2.getAccNumber(), dto2.getAccNumber());
        assertEquals(account2.getAccBalance(), dto2.getAccBalance());
        assertEquals(team2.getTeamName(), dto2.getTeamName());
    }

    @Test
    @DisplayName("전체 계좌 조회 테스트")
    void accounts() {
        // Given
        when(memberRepository.findById(memberIdx)).thenReturn(Optional.of(member));
        when(accountRepository.findAccountsByMember(any(Member.class))).thenReturn(Arrays.asList(account1, account2));

        // When
        List<AccountsResDto> result = accountService.accounts(memberIdx);

        // Then
        assertEquals(2, result.size());

        AccountsResDto dto1 = result.get(0);
        assertEquals(account1.getAccIdx(), dto1.getAccIdx());
        assertEquals(account1.getAccNumber(), dto1.getAccNumber());
        assertEquals(account1.getAccName(), dto1.getAccName());

        AccountsResDto dto2 = result.get(1);
        assertEquals(account2.getAccIdx(), dto2.getAccIdx());
        assertEquals(account2.getAccNumber(), dto2.getAccNumber());
        assertEquals(account2.getAccName(), dto2.getAccName());
    }
}
