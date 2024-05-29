package com.hanaro.triptogether.tripReply.service;


import com.hanaro.triptogether.enumeration.TeamMemberState;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.service.impl.TeamMemberServiceImpl;
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
        TeamMember teamMember = createTeamMember(1L,Mockito.mock(Team.class), TeamMemberState.요청중);
        given(teamMemberService.findTeamMemberByTeamMemberIdx(dto.getTeam_member_idx())).willReturn(teamMember);
        given(tripReplyService.validateAndReturn(trip_place_idx, dto.getTeam_member_idx())).willThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER_ROLE));
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
        TeamMember teamMember = createTeamMember(1L, team1, TeamMemberState.총무);
        given(teamMemberService.findTeamMemberByTeamMemberIdx(dto.getTeam_member_idx())).willReturn(teamMember);
        given(tripPlaceService.findTeamIdByTripPlaceIdx(trip_place_idx)).willReturn(team2.getTeamIdx());

        //when
        ApiException exception = assertThrows(ApiException.class, ()->tripReplyService.createReply(trip_place_idx, dto));

        //then
        assertEquals( ExceptionEnum.TRIP_INFO_NOT_MATCH.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).save(any(TripReply.class));
    }
    @Test
    void createReply_success() {
        //given
        Long trip_place_idx=1L;
        TripReplyReqDto dto = createTripReplyReqDto();
        TeamMember teamMember = createTeamMember(1L,Mockito.mock(Team.class), TeamMemberState.총무);
        given(teamMemberService.findTeamMemberByTeamMemberIdx(dto.getTeam_member_idx())).willReturn(teamMember);

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
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getTeam_member_idx());
        given(tripReplyRepository.findById(dto.getTrip_reply_idx())).willReturn(Optional.empty());

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
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getTeam_member_idx());
        given(tripReplyRepository.findById(dto.getTrip_reply_idx())).willReturn(Optional.of(tripReply));

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
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getTeam_member_idx());
        given(tripReplyRepository.findById(dto.getTrip_reply_idx())).willReturn(Optional.of(tripReply));

        // when
        tripReplyService.updateReply(trip_place_idx, dto);

        // then
        assertEquals("updated content", tripReply.getTripReplyContent());
        assertEquals("updated content", tripReplyRepository.findById(dto.getTrip_reply_idx()).get().getTripReplyContent());
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
        then(tripReplyRepository).should(never()).deleteById(dto.getTrip_reply_idx());
    }

    @Test
    void deleteReply_notFoundReply() {
        // given
        Long trip_place_idx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getTeam_member_idx());
        given(tripReplyRepository.findById(dto.getTrip_reply_idx())).willReturn(Optional.empty());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.deleteReply(trip_place_idx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).deleteById(dto.getTrip_reply_idx());
    }

    @Test
    void deleteReply_notSameMember() {
        // given
        Long trip_place_idx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        TripReply tripReply = createTripReply(createTeamMember(2L));
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getTeam_member_idx());
        given(tripReplyRepository.findById(dto.getTrip_reply_idx())).willReturn(Optional.of(tripReply));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripReplyService.deleteReply(trip_place_idx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_REPLY_MEMBER_NOT_MATCH.getMessage(), exception.getMessage());
        then(tripReplyRepository).should(never()).deleteById(dto.getTrip_reply_idx());
    }

    @Test
    void deleteReply_success() {
        // given
        Long trip_place_idx = 1L;
        TripReplyDeleteReqDto dto = createTripReplyDeleteReqDto();
        TeamMember teamMember = createTeamMember(1L);
        TripReply tripReply = createTripReply(teamMember);
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getTeam_member_idx());
        given(tripReplyRepository.findById(dto.getTrip_reply_idx())).willReturn(Optional.of(tripReply));

        // when
        tripReplyService.deleteReply(trip_place_idx, dto);

        // then
        then(tripReplyRepository).should(times(1)).deleteById(dto.getTrip_reply_idx());
    }
    private TripReplyReqDto createTripReplyReqDto() {
        return TripReplyReqDto.builder()
                .trip_reply_content("test")
                .team_member_idx(1L)
                .build();
    }

    private TripReplyUpdateReqDto createTripReplyUpdateReqDto() {
        return TripReplyUpdateReqDto.builder()
                .trip_reply_idx(1L)
                .trip_reply_content("updated content")
                .team_member_idx(1L)
                .build();
    }

    private TripReplyDeleteReqDto createTripReplyDeleteReqDto() {
        return TripReplyDeleteReqDto.builder()
                .trip_reply_idx(1L)
                .team_member_idx(1L)
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