package com.hanaro.triptogether.tripReply.service;

import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.service.impl.TeamMemberServiceImpl;
import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import com.hanaro.triptogether.tripPlace.service.TripPlaceService;
import com.hanaro.triptogether.tripReply.domain.TripReply;
import com.hanaro.triptogether.tripReply.domain.TripReplyRepository;
import com.hanaro.triptogether.tripReply.dto.request.TripReplyDeleteReqDto;
import com.hanaro.triptogether.tripReply.dto.request.TripReplyReqDto;
import com.hanaro.triptogether.tripReply.dto.request.TripReplyUpdateReqDto;
import com.hanaro.triptogether.tripReply.dto.response.TripReplyResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hanaro.triptogether.util.Constants.DELETED_MEMBER;

@Service
@RequiredArgsConstructor
public class TripReplyService {
    private final TripPlaceService tripPlaceService;
    private final TeamMemberServiceImpl teamMemberService;
    private final TripReplyRepository tripReplyRepository;

    @Transactional
    public void createReply(Long trip_place_idx, TripReplyReqDto dto) {
        TripPlace tripplace = tripPlaceService.checkTripPlaceExists(trip_place_idx);
        TeamMember teamMember =validateAndReturn(trip_place_idx, tripplace.getTrip().getTeam().getTeamIdx(), dto.getMemberIdx());
        tripReplyRepository.save(dto.toEntity(tripplace, teamMember, dto.getTripReplyContent()));
    }
    @Transactional
    public void updateReply(Long trip_place_idx, TripReplyUpdateReqDto dto) {
        tripPlaceService.checkTripPlaceExists(trip_place_idx);

        TripPlace tripplace = tripPlaceService.checkTripPlaceExists(trip_place_idx);
        validateAndReturn(trip_place_idx, tripplace.getTrip().getTeam().getTeamIdx(), dto.getMemberIdx());

        TripReply tripReply = checkTripReplyExist(dto.getTripReplyIdx());
        checkSameMember(tripReply.getTeamMember().getTeamMemberIdx(),dto.getMemberIdx());

        tripReply.update(dto.getTripReplyContent());
    }

    @Transactional
    public void deleteReply(Long trip_place_idx, TripReplyDeleteReqDto dto) {

        TripPlace tripplace = tripPlaceService.checkTripPlaceExists(trip_place_idx);
        validateAndReturn(trip_place_idx, tripplace.getTrip().getTeam().getTeamIdx(), dto.getMemberIdx());

        TripReply tripReply = checkTripReplyExist(dto.getTripReplyIdx());
        checkSameMember(tripReply.getTeamMember().getTeamMemberIdx(),dto.getMemberIdx());

        tripReplyRepository.deleteById(dto.getTripReplyIdx());
    }

    public List<TripReplyResDto> getReply(Long trip_place_idx) {
        tripPlaceService.checkTripPlaceExists(trip_place_idx);

        return tripReplyRepository
                .findAllByTripPlace_TripPlaceIdxOrderByCreatedAtAsc(trip_place_idx)
                .stream()
                .map(this::mapToTripReplyResDto)
                .collect(Collectors.toList());
    }

    private TripReplyResDto mapToTripReplyResDto(TripReply tripReply) {
        String memberName = tripReply.getTeamMember().getMember().getDeletedAt() == null
                ? tripReply.getTeamMember().getMember().getMemberName()
                : DELETED_MEMBER;

        return new TripReplyResDto(tripReply, memberName);
    }

    private TripReply checkTripReplyExist(Long reply_idx){
        return tripReplyRepository.findById(reply_idx).orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_REPLY_NOT_FOUND));
    }

    private void checkSameMember(Long m1, Long m2){
        //작성자와 요청가 일치하지 않는 경우
        if(!Objects.equals(m1, m2)){
            throw new ApiException(ExceptionEnum.TRIP_REPLY_MEMBER_NOT_MATCH);
        }
    }

    TeamMember validateAndReturn(Long tripPlaceIdx,Long teamIdx, Long memberIdx) {
        TeamMember teamMember = teamMemberService.findTeamMemberByMemberIdxAndTeamIdx(memberIdx, teamIdx);
        validateTeam( tripPlaceIdx, teamMember.getTeam().getTeamIdx());
        validateTeamMember( teamMember.getTeamMemberIdx());
        return teamMember;
    }

    private void validateTeam(Long trip_place_idx, Long teamIdx){
        //place의 팀과 teamMember의 팀이 일치하지 않는 경우
        if(!Objects.equals(tripPlaceService.findTeamIdByTripPlaceIdx(trip_place_idx), teamIdx)){
            throw new ApiException(ExceptionEnum.TRIP_INFO_NOT_MATCH);
        }
    }

    private void validateTeamMember(Long team_member_idx) {
        TeamMember teamMember = teamMemberService.checkIsMyTeamByTeamMemberIdx(team_member_idx);
        //유효한 상태인지 확인
        teamMemberService.validateTeamMemberState(teamMember);
    }

}
