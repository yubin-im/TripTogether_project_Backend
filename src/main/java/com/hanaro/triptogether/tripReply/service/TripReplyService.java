package com.hanaro.triptogether.tripReply.service;

import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.service.impl.TeamMemberServiceImpl;
import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import com.hanaro.triptogether.tripPlace.service.TripPlaceService;
import com.hanaro.triptogether.tripReply.domain.TripReply;
import com.hanaro.triptogether.tripReply.domain.TripReplyRepository;
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
        TeamMember teamMember = teamMemberService.findTeamMemberByTeamMemberIdx(dto.getTeam_member_idx());

        //place의 팀과 teamMember의 팀이 일치하지 않는 경우
        if(!Objects.equals(tripPlaceService.findTeamIdByTripPlaceIdx(trip_place_idx), teamMember.getTeam().getTeamIdx())){
            throw new ApiException(ExceptionEnum.TRIP_INFO_NOT_MATCH);
        };

        tripReplyRepository.save(dto.toEntity(tripplace, teamMember, dto.getTrip_reply_content()));
    }
    @Transactional
    public void updateReply(Long trip_place_idx, TripReplyUpdateReqDto dto) {
        tripPlaceService.checkTripPlaceExists(trip_place_idx);
        TeamMember teamMember = teamMemberService.findTeamMemberByTeamMemberIdx(dto.getTeam_member_idx());

        //place의 팀과 teamMember의 팀이 일치하지 않는 경우
        if(!Objects.equals(tripPlaceService.findTeamIdByTripPlaceIdx(trip_place_idx), teamMember.getTeam().getTeamIdx())){
            throw new ApiException(ExceptionEnum.TRIP_INFO_NOT_MATCH);
        };

        TripReply tripReply = tripReplyRepository.findById(dto.getTrip_reply_idx()).orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_REPLY_NOT_FOUND));

        //작성자와 수정자가 일치하지 않는 경우
        if(!Objects.equals(tripReply.getTeamMember(),teamMember)){
            throw new ApiException(ExceptionEnum.TRIP_REPLY_MEMBER_NOT_MATCH);
        }
        tripReply.update(dto.getTrip_reply_content());
    }

    public List<TripReplyResDto> getReply(Long trip_place_idx) {
        tripPlaceService.checkTripPlaceExists(trip_place_idx);

        return tripReplyRepository
                .findAllByTripPlace_TripPlaceIdxOrderByCreatedAtAsc(trip_place_idx)
                .stream()
                .map(this::mapToTripReplyResDto)
                .collect(Collectors.toList());
    }

    public void deleteReply(Long trip_place_idx, Long reply_idx) {
        tripPlaceService.checkTripPlaceExists(trip_place_idx);
        checkTripReplyExist(reply_idx);
        tripReplyRepository.deleteById(reply_idx);
    }

    private TripReplyResDto mapToTripReplyResDto(TripReply tripReply) {
        String memberName = tripReply.getTeamMember().getMember().getDeletedAt() == null
                ? tripReply.getTeamMember().getMember().getMemberName()
                : DELETED_MEMBER;

        return new TripReplyResDto(tripReply, memberName);
    }

    public void checkTripReplyExist(Long reply_idx){
        tripReplyRepository.findById(reply_idx).orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_REPLY_NOT_FOUND));
    }
}
