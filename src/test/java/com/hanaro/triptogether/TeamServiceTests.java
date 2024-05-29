package com.hanaro.triptogether;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.team.dto.request.*;
import com.hanaro.triptogether.team.dto.response.*;
import com.hanaro.triptogether.team.service.impl.TeamServiceImpl;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TeamServiceTests extends TriptogetherApplicationTests {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    TeamServiceImpl teamService;

    @Test
    @DisplayName("모임서비스 상세 테스트")
    void detailTeam() {
        // Given
        Long accIdx = 1L;

        Account account = Account.builder()
                .accIdx(accIdx)
                .accNumber("12345")
                .accBalance(BigDecimal.valueOf(1000.00))
                .build();

        Team team = Team.builder()
                .teamIdx(1L)
                .teamNotice("Team Notice")
                .teamName("Team Name")
                .account(account)
                .build();

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(teamRepository.findTeamByAccount(any())).thenReturn(team);

        // When
        DetailTeamResDto result = teamService.detailTeam(accIdx);

        // Then
        assertNotNull(result);
        assertEquals(team.getTeamIdx(), result.getTeamIdx());
        assertEquals(team.getTeamNotice(), result.getTeamNotice());
        assertEquals(team.getTeamName(), result.getTeamName());
        assertEquals(account.getAccNumber(), result.getAccNumber());
        assertEquals(account.getAccBalance(), result.getAccBalance());

        verify(accountRepository, times(1)).findById(accIdx);
        verify(teamRepository, times(1)).findTeamByAccount(account);
    }

    @Test
    @DisplayName("모임서비스 관리 테스트")
    void manageTeam() {
        // Given
        Long teamIdx = 1L;

        ManageTeamReqDto manageTeamReqDto = ManageTeamReqDto.builder()
                .teamIdx(teamIdx)
                .memberIdx(1L)
                .build();

        Member member = Member.builder()
                .alarmStatus(true)
                .build();

        Account account = Account.builder()
                .accNumber("12345")
                .accBalance(BigDecimal.valueOf(1000.00))
                .member(member)
                .build();

        Team team = Team.builder()
                .teamName("Team Name")
                .account(account)
                .build();

        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        // When
        ManageTeamResDto result = teamService.manageTeam(manageTeamReqDto);

        // Then
        assertNotNull(result);
        assertEquals(team.getTeamName(), result.getTeamName());
        assertEquals(account.getAccNumber(), result.getAccNumber());
        assertEquals(account.getAccBalance(), result.getAccBalance());
        assertEquals(member.getAlarmStatus(), result.getAlarmStatus());

        verify(teamRepository, times(1)).findById(teamIdx);
    }

    @Test
    @DisplayName("모임서비스 초대하기 테스트")
    void generateInviteLink() {
        // Given
        Long teamIdx = 1L;
        Long memberIdx = 1L;

        InviteTeamReqDto inviteTeamReqDto = InviteTeamReqDto.builder()
                .memberIdx(memberIdx)
                .teamIdx(teamIdx)
                .build();

        Member member = Member.builder()
                .memberName("memberName")
                .build();

        Team team = Team.builder()
                .teamIdx(teamIdx)
                .teamName("Team Name")
                .build();

        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        // When
        String result = teamService.generateInviteLink(inviteTeamReqDto);

        // Then
        String expectedUrl = "http://localhost:8080/invite?inviter=memberName&teamNo=" + teamIdx;
        String expectedMessage = "[하나은행]\nmemberName님이 Team Name에 초대했어요.\n" + expectedUrl;
        assertEquals(expectedMessage, result);

        verify(teamRepository, times(1)).findById(teamIdx);
        verify(memberRepository, times(1)).findById(memberIdx);
    }

    @Test
    @DisplayName("모임 초대받은 화면 테스트")
    void inviteTeam() {
        // Given
        String inviter = "memberName";
        Long teamNo = 1L;

        Team team = Team.builder()
                .teamIdx(teamNo)
                .teamName("Team Name")
                .build();

        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        // When
        InviteTeamResDto result = teamService.inviteTeam(inviter, teamNo);

        // Then
        assertNotNull(result);
        assertEquals(inviter, result.getInviter());
        assertEquals("Team Name", result.getTeamName());
        assertEquals(teamNo, result.getTeamNo());

        verify(teamRepository, times(1)).findById(teamNo);
    }
}

