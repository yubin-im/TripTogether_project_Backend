package com.hanaro.triptogether;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.enumeration.PreferenceType;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.team.dto.request.*;
import com.hanaro.triptogether.team.dto.response.*;
import com.hanaro.triptogether.team.service.impl.TeamServiceImpl;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import com.hanaro.triptogether.trip.domain.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TeamServiceTests {
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

    private Account account;
    private Long accIdx = 1L;
    private Team team;
    private Long teamIdx = 1L;
    private Member member;
    private Long memberIdx = 1L;
    private TeamMember teamMember;
    private Long teamMemberIdx = 1L;
    private Trip trip;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = Member.builder()
                .memberIdx(memberIdx)
                .memberId("testId")
                .memberPw("testPw")
                .alarmStatus(true)
                .memberLoginPw("123456")
                .memberName("memberName")
                .createdAt(LocalDateTime.now())
                .build();

        account = Account.builder()
                .accIdx(accIdx)
                .member(member)
                .accNumber("12345")
                .accBalance(BigDecimal.valueOf(1000.00))
                .cardStatus(false)
                .accName("Acc Name")
                .createdAt(LocalDateTime.now())
                .build();

        trip = Trip.builder()
                .tripIdx(1L)
                .build();

        team = Team.builder()
                .teamIdx(teamIdx)
                .account(account)
                .teamName("Team Name")
                .preferenceType(PreferenceType.해외)
                .teamNotice("Team Notice")
                .createdAt(LocalDateTime.now())
                .preferTrip(trip)
                .build();

        teamMember = TeamMember.builder()
                .teamMemberIdx(teamMemberIdx)
                .team(team)
                .member(member)
                .build();
    }

    @Test
    @DisplayName("모임서비스 상세 테스트")
    void testDetailTeam() {
        // Given
        DetailTeamReqDto detailTeamReqDto = DetailTeamReqDto.builder()
                .teamIdx(teamIdx)
                .teamMemberIdx(teamMemberIdx)
                .build();

        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        when(teamMemberRepository.findById(anyLong())).thenReturn(Optional.of(teamMember));

        // When
        DetailTeamResDto result = teamService.detailTeam(detailTeamReqDto);

        // Then
        assertNotNull(result);
        assertEquals(team.getTeamNotice(), result.getTeamNotice());
        assertEquals(team.getTeamName(), result.getTeamName());
        assertEquals(team.getAccount().getAccNumber(), result.getAccNumber());
        assertEquals(team.getAccount().getAccBalance(), result.getAccBalance());
        assertEquals(teamMember.getTeamMemberState(), result.getTeamMemberState());

        verify(teamRepository, times(1)).findById(teamIdx);
        verify(teamMemberRepository, times(1)).findById(teamMemberIdx);
    }

    @Test
    @DisplayName("모임서비스 관리 테스트")
    void testManageTeam() {
        // Given
        ManageTeamReqDto manageTeamReqDto = ManageTeamReqDto.builder()
                .teamIdx(teamIdx)
                .memberIdx(memberIdx)
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
        assertEquals(account.getAccIdx(), result.getAccIdx());

        verify(teamRepository, times(1)).findById(teamIdx);
    }

    @Test
    @DisplayName("모임서비스 초대하기 테스트")
    void testGenerateInviteLink() {
        // Given
        InviteTeamReqDto inviteTeamReqDto = InviteTeamReqDto.builder()
                .memberIdx(memberIdx)
                .teamIdx(teamIdx)
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
    void testInviteTeam() {
        // Given
        String inviter = "memberName";
        Long teamNo = 1L;

        Team team2 = Team.builder()
                .teamIdx(teamNo)
                .teamName("Team Name")
                .build();

        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team2));

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

