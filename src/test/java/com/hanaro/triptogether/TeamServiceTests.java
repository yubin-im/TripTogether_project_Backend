package com.hanaro.triptogether;


import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.account.domain.AccountRepository;
import com.hanaro.triptogether.enumeration.PreferenceType;
import com.hanaro.triptogether.enumeration.TeamType;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.team.dto.request.AddTeamReqDto;
import com.hanaro.triptogether.team.dto.response.DetailTeamResDto;
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

public class TeamServiceTests extends TriptogetherApplicationTests{
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
    @DisplayName("모임서비스 가입 테스트")
    void addTeam() {
        // Given
        AddTeamReqDto addTeamReqDto = new AddTeamReqDto();
        addTeamReqDto.setMemberIdx(1L);
        addTeamReqDto.setAccIdx(1L);
        addTeamReqDto.setTeamType(TeamType.여행);
        addTeamReqDto.setTeamName("Test Team");
        addTeamReqDto.setPreferenceType(PreferenceType.해외);

        // When
        teamService.addTeam(addTeamReqDto);
    }

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

}

