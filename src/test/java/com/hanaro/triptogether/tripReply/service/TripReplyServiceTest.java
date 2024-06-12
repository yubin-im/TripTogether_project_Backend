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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

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
        // given
        Long tripPlaceIdx = 1L;
        TripReplyReqDto dto = createTripReplyReqDto();
        given(tripPlaceService.checkTripPlaceExists(tripPlaceIdx)).willThrow(new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.createReply(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_PLACE_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void createReply_invalidMemberState() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyReqDto dto = createTripReplyReqDto();
        Team team = createTeam(1L);
        TeamMember teamMember = createTeamMember(1L, team, TeamMemberState.요청중);
        Trip trip = createTrip(1L, team);

        given(tripPlaceService.checkTripPlaceExists(tripPlaceIdx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), team.getTeamIdx())).willReturn(teamMember);
        given(tripPlaceService.findTeamIdByTripPlaceIdx(tripPlaceIdx)).willReturn(team.getTeamIdx());
        given(tripReplyService.validateAndReturn(team.getTeamIdx(), tripPlaceIdx, dto.getMemberIdx())).willThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER_ROLE));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.createReply(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.INVALID_TEAM_MEMBER_ROLE.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void createReply_invalidMemberTeam() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyReqDto dto = createTripReplyReqDto();
        Team team1 = createTeam(1L);
        Team team2 = createTeam(2L);
        Trip trip = createTrip(1L, team1);

        given(tripPlaceService.checkTripPlaceExists(tripPlaceIdx)).willReturn(TripPlace.builder().trip(trip).build());
        given(teamMemberRepository.findTeamMemberByMember_MemberIdxAndTeam_TeamIdx(dto.getMemberIdx(), team2.getTeamIdx())).willReturn(Optional.empty());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), trip.getTeam().getTeamIdx())).willThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.createReply(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.INVALID_TEAM_MEMBER.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void createReply_success() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyReqDto dto = createTripReplyReqDto();
        Team team1 = createTeam(1L);
        Trip trip = createTrip(1L, team1);
        TeamMember teamMember = createTeamMember(1L, team1, TeamMemberState.총무);

        given(tripPlaceService.checkTripPlaceExists(tripPlaceIdx)).willReturn(TripPlace.builder().trip(trip).build());
        given(tripPlaceService.findTeamIdByTripPlaceIdx(tripPlaceIdx)).willReturn(team1.getTeamIdx());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(dto.getMemberIdx(), team1.getTeamIdx())).willReturn(teamMember);

        // when
        tripReplyService.createReply(tripPlaceIdx, dto);

        // then
        then(tripReplyRepository).should(times(1)).save(any(TripReply.class));
    }

    @Test
    void updateReply_invalidTripPlace() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyUpdateReqDto dto = createTripReplyUpdateReqDto();
        given(tripPlaceService.checkTripPlaceExists(tripPlaceIdx)).willThrow(new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.updateReply(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_PLACE_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void updateReply_notFoundReply() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyUpdateReqDto dto = createTripReplyUpdateReqDto();
        mockValidTripPlaceAndTeamMember(tripPlaceIdx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.empty());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.updateReply(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void updateReply_notSameMember() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyUpdateReqDto dto = createTripReplyUpdateReqDto();
        TripReply tripReply = createTripReply(createTeamMember(2L));
        mockValidTripPlaceAndTeamMember(tripPlaceIdx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.of(tripReply));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.updateReply(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_MEMBER_NOT_MATCH.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }

    @Test
    void updateReply_success() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyUpdateReqDto dto = createTripReplyUpdateReqDto();
        TeamMember teamMember = createTeamMember(1L);
        TripReply tripReply = createTripReply(teamMember);
        mockValidTripPlaceAndTeamMember(tripPlaceIdx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.of(tripReply));

        // when
        tripReplyService.updateReply(tripPlaceIdx, dto);

        // then
        assertEquals("updated content", tripReply.getTripReplyContent());
        assertEquals("updated content", tripReplyRepository.findById(dto.getTripReplyIdx()).get().getTripReplyContent());
    }

    @Test
    void deleteReply_invalidTripPlace() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        given(tripPlaceService.checkTripPlaceExists(tripPlaceIdx)).willThrow(new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.deleteReply(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_PLACE_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).deleteById(dto.getTripReplyIdx());
    }

    @Test
    void deleteReply_notFoundReply() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        mockValidTripPlaceAndTeamMember(tripPlaceIdx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.empty());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.deleteReply(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).deleteById(dto.getTripReplyIdx());
    }

    @Test
    void deleteReply_notSameMember() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        TripReply tripReply = createTripReply(createTeamMember(2L));
        mockValidTripPlaceAndTeamMember(tripPlaceIdx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.of(tripReply));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.deleteReply(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_MEMBER_NOT_MATCH.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).deleteById(dto.getTripReplyIdx());
    }

    @Test
    void deleteReply_success() {
        // given
        Long tripPlaceIdx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        TeamMember teamMember = createTeamMember(1L);
        TripReply tripReply = createTripReply(teamMember);
        mockValidTripPlaceAndTeamMember(tripPlaceIdx, dto.getMemberIdx());
        given(tripReplyRepository.findById(dto.getTripReplyIdx())).willReturn(Optional.of(tripReply));

        // when
        tripReplyService.deleteReply(tripPlaceIdx, dto);

        // then
        then(tripReplyRepository).should(times(1)).deleteById(dto.getTripReplyIdx());
    }

    private void mockValidTripPlaceAndTeamMember(Long tripPlaceIdx, Long memberIdx) {
        Team team = createTeam(1L);
        Trip trip = createTrip(1L, team);
        TeamMember teamMember = createTeamMember(memberIdx, team, TeamMemberState.총무);

        given(tripPlaceService.checkTripPlaceExists(tripPlaceIdx)).willReturn(TripPlace.builder().trip(trip).build());
        given(tripPlaceService.findTeamIdByTripPlaceIdx(tripPlaceIdx)).willReturn(team.getTeamIdx());
        given(teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(memberIdx, team.getTeamIdx())).willReturn(teamMember);
    }

    private TripReplyReqDto createTripReplyReqDto() {
        return TripReplyReqDto.builder()
                .memberIdx(1L)
                .tripReplyContent("test content")
                .build();
    }

    private TripReplyUpdateReqDto createTripReplyUpdateReqDto() {
        return TripReplyUpdateReqDto.builder()
                .tripReplyIdx(1L)
                .memberIdx(1L)
                .tripReplyContent("updated content")
                .build();
    }

    private TripReplyDeleteReqDto createTripReplyDeleteReqDto() {
        return TripReplyDeleteReqDto.builder()
                .tripReplyIdx(1L)
                .memberIdx(1L)
                .build();
    }

    private Team createTeam(Long teamIdx) {
        return Team.builder()
                .teamIdx(teamIdx)
                .teamName("Test Team")
                .build();
    }

    private TeamMember createTeamMember(Long memberIdx) {
        return createTeamMember(memberIdx, createTeam(1L), TeamMemberState.총무);
    }

    private TeamMember createTeamMember(Long memberIdx, Team team, TeamMemberState state) {
        return TeamMember.builder()
                .teamMemberIdx(1L)
                .team(team)
                .teamMemberState(state)
                .member(createMember(memberIdx))
                .build();
    }

    private Member createMember(Long memberIdx) {
        return Member.builder()
                .memberIdx(memberIdx)
                .memberName("Test Member")
                .build();
    }

    private Trip createTrip(Long tripIdx, Team team) {
        return Trip.builder()
                .tripIdx(tripIdx)
                .tripName("Test Trip")
                .team(team)
                .build();
    }

    private TripReply createTripReply(TeamMember teamMember) {
        return TripReply.builder()
                .tripReplyContent("test content")
                .tripPlace(TripPlace.builder().trip(createTrip(1L, createTeam(1L))).build())
                .teamMember(teamMember)
                .build();
    }
}
