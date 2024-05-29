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
import com.hanaro.triptogether.tripReply.dto.request.TripReplyReqDto;
import com.hanaro.triptogether.tripReply.dto.request.TripReplyUpdateReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        TripReplyReqDto dto = TripReplyReqDto.builder()
                .trip_reply_content("test")
                .team_member_idx(1L).build();
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
        TripReplyReqDto dto = TripReplyReqDto.builder()
                .trip_reply_content("test")
                .team_member_idx(1L).build();
        TeamMember teamMember = TeamMember.builder()
                        .team(Mockito.mock(Team.class))
                        .teamMemberIdx(1L)
                        .teamMemberState(TeamMemberState.요청중).build();
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
        TripReplyReqDto dto = TripReplyReqDto.builder()
                .trip_reply_content("test")
                .team_member_idx(1L).build();
        Team team1 = Team.builder()
                .teamIdx(1L)
                .teamName("teamName")
                .build();
        Team team2 = Team.builder()
                .teamIdx(2L)
                .teamName("teamName")
                .build();
        TeamMember teamMember = TeamMember.builder()
                .team(team1)
                .teamMemberIdx(1L).build();
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
        TripReplyReqDto dto = TripReplyReqDto.builder()
                .trip_reply_content("test")
                .team_member_idx(1L).build();
        TeamMember teamMember = TeamMember.builder()
                .team(Mockito.mock(Team.class))
                .teamMemberIdx(1L)
                .teamMemberState(TeamMemberState.총무).build();
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
        TripReply tripReply = TripReply.builder()
                .tripReplyContent("original content")
                .teamMember(TeamMember.builder().teamMemberIdx(2L).build()) // 다른 작성자
                .build();
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
        TeamMember teamMember = TeamMember.builder()
                .teamMemberIdx(1L)
                .team(Mockito.mock(Team.class))
                .build();
        TripReply tripReply = TripReply.builder()
                .tripReplyContent("original content")
                .teamMember(teamMember)
                .build();
        mockValidTripPlaceAndTeamMember(trip_place_idx, dto.getTeam_member_idx());
        given(tripReplyRepository.findById(dto.getTrip_reply_idx())).willReturn(Optional.of(tripReply));

        // when
        tripReplyService.updateReply(trip_place_idx, dto);

        // then
        assertEquals("updated content", tripReply.getTripReplyContent());
        assertEquals("updated content", tripReplyRepository.findById(dto.getTrip_reply_idx()).get().getTripReplyContent());
    }

    private TripReplyUpdateReqDto createTripReplyUpdateReqDto() {
        return TripReplyUpdateReqDto.builder()
                .trip_reply_idx(1L)
                .trip_reply_content("updated content")
                .team_member_idx(1L)
                .build();
    }

    private void mockValidTripPlaceAndTeamMember(Long trip_place_idx, Long team_member_idx) {
        Team team = Team.builder()
                .teamIdx(1L)
                .teamName("teamName")
                .build();
        TeamMember teamMember = TeamMember.builder()
                .teamMemberIdx(team_member_idx)
                .team(team)
                .build();
        given(tripPlaceService.checkTripPlaceExists(trip_place_idx)).willReturn(Mockito.mock(TripPlace.class));
        given(tripPlaceService.findTeamIdByTripPlaceIdx(trip_place_idx)).willReturn(team.getTeamIdx());
        given(teamMemberService.findTeamMemberByTeamMemberIdx(team_member_idx)).willReturn(teamMember);
    }
    @Test
    void deleteReply() {
    }

    @Test
    void getReply() {
    }
}