package com.hanaro.triptogether.tripReply.service;


import com.hanaro.triptogether.enumeration.TeamMemberState;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.domain.TeamMemberRepository;
import com.hanaro.triptogether.teamMember.service.impl.TeamMemberServiceImpl;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import com.hanaro.triptogether.tripPlace.service.TripPlaceService;
import com.hanaro.triptogether.tripReply.domain.TripReply;
import com.hanaro.triptogether.tripReply.domain.TripReplyRepository;
import com.hanaro.triptogether.tripReply.dto.request.TripReplyDeleteReqDto;
import com.hanaro.triptogether.tripReply.dto.request.TripReplyReqDto;
import com.hanaro.triptogether.tripReply.dto.request.TripReplyUpdateReqDto;
import com.hanaro.triptogether.tripReply.dto.response.TripReplyResDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.hanaro.triptogether.util.Constants.DELETED_MEMBER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class TripReplyServiceTest {

    @Mock
    private TripPlaceService tripPlaceService;
    @Mock
    private TeamMemberServiceImpl teamMemberService;
    @Mock
    private TripReplyRepository tripReplyRepository;
    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Spy
    @InjectMocks
    private TripReplyService tripReplyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReply_invalidTripPlace() {
        //given
        Long trip_place_idx=1L;
        TripReplyReqDto dto = createTripReplyReqDto();
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willThrow(new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));

        //when
        ApiException exception = assertThrows(ApiException.class, ()->tripReplyService.createReply(trip_place_idx, dto));

        //then
        assertEquals( ExceptionEnum.TRIP_PLACE_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }
    @Test
    void createReply_invalidMemberState() {
        //given
        Long trip_place_idx=1L;
        TripReplyReqDto dto = createTripReplyReqDto();
        Team team = createTeam(1L);
        TeamMember teamMember = createTeamMember(1L,team, TeamMemberState.요청중);
        Trip trip = Trip.builder()
                .tripIdx(1L)
                .team(team)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();

        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), team.getTeamIdx())).willReturn(teamMember);
        given(tripPlaceService.findTeamIdByTripPlaceIdx(trip_place_idx)).willReturn(team.getTeamIdx());
        given(tripReplyService.validateAndReturn(team.getTeamIdx(), trip_place_idx, dto.getMemberIdx())).willThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER_ROLE));
        //when
        ApiException exception = assertThrows(ApiException.class, ()->tripReplyService.createReply(trip_place_idx, dto));

        //then
        assertEquals( ExceptionEnum.INVALID_TEAM_MEMBER_ROLE.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void createReply_invalidMemberTeam() {
        //given
        Long trip_place_idx=1L;
        TripReplyReqDto dto = createTripReplyReqDto();
        Team team1 = createTeam(1L);
        Team team2 = createTeam(2L);

        Trip trip = Trip.builder()
                .tripIdx(1L)
                .team(team1)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();

        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberRepository.findTeamMemberByMember_MemberIdxAndTeam_TeamIdx(dto.getMemberIdx(), team2.getTeamIdx())).willReturn(Optional.empty());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), trip.getTeam().getTeamIdx())).willThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER));

        //when
        ApiException exception = assertThrows(ApiException.class, ()->tripReplyService.createReply(trip_place_idx, dto));

        //then
        assertEquals( ExceptionEnum.INVALID_TEAM_MEMBER.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }
    @Test
    void createReply_success() {
        //given
        Long trip_place_idx=1L;
        TripReplyReqDto dto = createTripReplyReqDto();
        Team team1 = createTeam(1L);
        Trip trip = Trip.builder()
                .tripIdx(1L)
                .team(team1)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        TeamMember teamMember = createTeamMember(1L,team1, TeamMemberState.총무);
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(TripPlace.builder().trip(trip).build());
        given(tripPlaceService.findTeamIdByTripPlaceIdx(trip_place_idx)).willReturn(team1.getTeamIdx());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), team1.getTeamIdx())).willReturn(teamMember);

        //when
        tripReplyService.createReply(trip_place_idx, dto);

        //then
        then(tripReplyRepository).should(times(1)).save(any(TripReply.class));
    }

    @Test
    void updateReply_invalidTripPlace() {
        // given
        Long trip_place_idx = 1L;
        TripReplyUpdateReqDto dto = createTripReplyUpdateReqDto();
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willThrow(new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.updateReply(trip_place_idx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_PLACE_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void updateReply_notFoundReply() {
        // given
        Long trip_place_idx = 1L;
        TripReplyUpdateReqDto dto = createTripReplyUpdateReqDto();
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.empty());
        TeamMember teamMember = createTeamMember(1L);
        Team team = createTeam(1L);
        Trip trip = Trip.builder()
                .tripIdx(1L)
                .team(team)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), trip.getTeam().getTeamIdx())).willReturn(teamMember);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.updateReply(trip_place_idx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void updateReply_notSameMember() {
        // given
        Long trip_place_idx = 1L;
        TripReplyUpdateReqDto dto = createTripReplyUpdateReqDto();
        TripReply tripReply = createTripReply(createTeamMember(2L));
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.of(tripReply));
        TeamMember teamMember = createTeamMember(1L);
        Team team = createTeam(1L);
        Trip trip = Trip.builder()
                .tripIdx(1L)
                .team(team)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), trip.getTeam().getTeamIdx())).willReturn(teamMember);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.updateReply(trip_place_idx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_MEMBER_NOT_MATCH.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void updateReply_success() {
        // given
        Long trip_place_idx = 1L;
        TripReplyUpdateReqDto dto = createTripReplyUpdateReqDto();
        TeamMember teamMember = createTeamMember(1L);
        TripReply tripReply = createTripReply(teamMember);
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.of(tripReply));
        Team team = createTeam(1L);
        Trip trip = Trip.builder()
                .tripIdx(1L)
                .team(team)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), trip.getTeam().getTeamIdx())).willReturn(teamMember);

        // when
        tripReplyService.updateReply(trip_place_idx, dto);

        // then
        assertEquals("updated content", tripReply.getTripReplyContent());
        assertEquals("updated content", tripReplyRepository.findById(dto.getTripReplyIdx()).get().getTripReplyContent());
    }

    @Test
    void deleteReply_invalidTripPlace() {
        // given
        Long trip_place_idx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willThrow(new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.deleteReply(trip_place_idx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_PLACE_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).deleteById(dto.getTripReplyIdx());
    }

    @Test
    void deleteReply_notFoundReply() {
        // given
        Long trip_place_idx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.empty());

        TeamMember teamMember = createTeamMember(1L);
        Team team = createTeam(1L);
        Trip trip = Trip.builder()
                .tripIdx(1L)
                .team(team)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), trip.getTeam().getTeamIdx())).willReturn(teamMember);
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.empty());


        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.deleteReply(trip_place_idx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).deleteById(dto.getTripReplyIdx());
    }

    @Test
    void deleteReply_notSameMember() {
        // given
        Long trip_place_idx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        TripReply tripReply = createTripReply(createTeamMember(2L));
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.of(tripReply));
        TeamMember teamMember = createTeamMember(1L);
        Team team = createTeam(1L);
        Trip trip = Trip.builder()
                .tripIdx(1L)
                .team(team)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), trip.getTeam().getTeamIdx())).willReturn(teamMember);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.deleteReply(trip_place_idx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_MEMBER_NOT_MATCH.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).deleteById(dto.getTripReplyIdx());
    }

    @Test
    void deleteReply_success() {
        // given
        Long trip_place_idx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        TeamMember teamMember = createTeamMember(1L);
        TripReply tripReply = createTripReply(teamMember);
        Team team = createTeam(1L);
        Trip trip = Trip.builder()
                .tripIdx(1L)
                .team(team)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .build();
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getMemberIdx());
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), trip.getTeam().getTeamIdx())).willReturn(teamMember);
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.of(tripReply));

        // when
        tripReplyService.deleteReply(trip_place_idx, dto);

        // then
        then(tripReplyRepository).should(times(1)).deleteById(dto.getTripReplyIdx());
    }

    @Test
    void getReply_invalidTripPlace() {
        // given
        given(tripPlaceService.checkTripPlaceExists(anyLong())).willThrow(new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));
        //when
        ApiException exception = assertThrows(ApiException.class, ()->tripReplyService.getReply(anyLong()));

        //then
        assertEquals( ExceptionEnum.TRIP_PLACE_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void getReply_success() {
        // given
        Long trip_place_idx = 1L;
        mockValidTripPlaceAndTeamMember(trip_place_idx, 1L);
        TeamMember teamMember = TeamMember.builder()
                .teamMemberIdx(1L).member(Member.builder().memberIdx(1L).memberName("test").build()).build();
        TeamMember teamMember2 = TeamMember.builder()
                .teamMemberIdx(2L).member(Member.builder().memberIdx(2L).memberName("test").deletedAt(LocalDateTime.now()).build()).build();

        TripReply tripReply1 = createTripReply(teamMember);
        TripReply tripReply2 = createTripReply(teamMember2);
        given(tripReplyRepository
                .findAllByTripPlace_TripPlaceIdxOrderByCreatedAtAsc(trip_place_idx)).willReturn(List.of(tripReply1, tripReply2));

        // when
        List<TripReplyResDto> res = tripReplyService.getReply(trip_place_idx);

        // then
        assertEquals(2, res.size());
        assertEquals(tripReply1.getTeamMember().getTeamMemberIdx(), res.get(0).getTeamMemberIdx());
        assertEquals(tripReply2.getTeamMember().getTeamMemberIdx(), res.get(1).getTeamMemberIdx());
        assertEquals(DELETED_MEMBER, res.get(1).getTeamMemberNickname()); //탈퇴한 멤버 닉네임 확인
    }

    private TripReplyReqDto createTripReplyReqDto() {
        return TripReplyReqDto.builder()
                .tripReplyContent("test")
                .memberIdx(1L)
                .build();
    }

    private TripReplyUpdateReqDto createTripReplyUpdateReqDto() {
        return TripReplyUpdateReqDto.builder()
                .tripReplyIdx(1L)
                .tripReplyContent("updated content")
                .memberIdx(1L)
                .build();
    }

    private TripReplyDeleteReqDto createTripReplyDeleteReqDto() {
        return TripReplyDeleteReqDto.builder()
                .tripReplyIdx(1L)
                .memberIdx(1L)
                .build();
    }

    private Team createTeam(Long teamId) {
        return Team.builder()
                .teamIdx(teamId)
                .teamName("teamName")
                .build();
    }

    private TeamMember createTeamMember(Long memberId) {
        return TeamMember.builder()
                .teamMemberIdx(memberId)
                .team(createTeam(1L))
                .build();
    }

    private TeamMember createTeamMember(Long memberId,Team team, TeamMemberState state) {
        return TeamMember.builder()
                .teamMemberIdx(memberId)
                .team(team)
                .teamMemberState(state)
                .build();
    }


    private TripReply createTripReply(TeamMember teamMember) {
        return TripReply.builder()
                .tripReplyContent("original content")
                .teamMember(teamMember)
                .build();
    }

    private void mockValidTripPlaceAndTeamMember(Long tripPlaceIdx, Long teamMemberIdx) {
        Team team = createTeam(1L);
        TeamMember teamMember = createTeamMember(teamMemberIdx, team, TeamMemberState.총무);
        given(tripPlaceService.checkTripPlaceExists(tripPlaceIdx)).willReturn(Mockito.mock(TripPlace.class));
        given(tripPlaceService.findTeamIdByTripPlaceIdx(tripPlaceIdx)).willReturn(team.getTeamIdx());
        given(teamMemberService.findTeamMemberByTeamMemberIdx(teamMemberIdx)).willReturn(teamMember);
    }
}