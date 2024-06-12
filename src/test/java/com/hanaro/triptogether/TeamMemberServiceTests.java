package com.hanaro.triptogether;

import com.hanaro.triptogether.enumeration.PreferenceType;
import com.hanaro.triptogether.enumeration.TeamMemberState;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import com.hanaro.triptogether.teamMember.dto.response.TeamMembersResDto;
import com.hanaro.triptogether.teamMember.service.impl.TeamMemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TeamMemberServiceTests {
    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    TeamMemberServiceImpl teamMemberService;

    private Team team;
    private Long teamIdx = 1L;
    private Member member1;
    private Member member2;
    private TeamMember teamMember1;
    private TeamMember teamMember2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member1 = Member.builder()
                .memberIdx(1L)
                .memberId("testId1")
                .memberPw("testPw1")
                .alarmStatus(true)
                .memberLoginPw("123456")
                .memberName("김하나")
                .createdAt(LocalDateTime.now())
                .build();

        member2 = Member.builder()
                .memberIdx(2L)
                .memberId("testId2")
                .memberPw("testPw2")
                .alarmStatus(true)
                .memberLoginPw("456789")
                .memberName("김은행")
                .createdAt(LocalDateTime.now())
                .build();

        team = Team.builder()
                .teamIdx(teamIdx)
                .teamName("Team Name")
                .preferenceType(PreferenceType.해외)
                .teamNotice("Team Notice")
                .createdAt(LocalDateTime.now())
                .build();

        teamMember1 = TeamMember.builder()
                .teamMemberIdx(1L)
                .team(team)
                .member(member1)
                .teamMemberState(TeamMemberState.모임원)
                .createdAt(LocalDateTime.now())
                .build();

        teamMember2 = TeamMember.builder()
                .teamMemberIdx(2L)
                .team(team)
                .member(member2)
                .teamMemberState(TeamMemberState.모임원)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("모임원 전체 출력 테스트")
    void testTeamMembers() {
        // Given
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        when(teamMemberRepository.findTeamMembersByTeam(any(Team.class)))
                .thenReturn(Arrays.asList(teamMember1, teamMember2));

        // When
        List<TeamMembersResDto> result = teamMemberService.teamMembers(teamIdx);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        TeamMembersResDto teamMemberDto1 = result.get(0);
        assertEquals(1L, teamMemberDto1.getTeamMemberIdx());
        assertEquals("김하나", teamMemberDto1.getMemberName());
        assertEquals(TeamMemberState.모임원, teamMemberDto1.getTeamMemberState());
        assertEquals(1L, teamMemberDto1.getMemberIdx());

        TeamMembersResDto teamMemberDto2 = result.get(1);
        assertEquals(2L, teamMemberDto2.getTeamMemberIdx());
        assertEquals("김은행", teamMemberDto2.getMemberName());
        assertEquals(TeamMemberState.모임원, teamMemberDto2.getTeamMemberState());
        assertEquals(2L, teamMemberDto2.getMemberIdx());

        verify(teamRepository, times(1)).findById(teamIdx);
        verify(teamMemberRepository, times(1)).findTeamMembersByTeam(team);
    }
}
