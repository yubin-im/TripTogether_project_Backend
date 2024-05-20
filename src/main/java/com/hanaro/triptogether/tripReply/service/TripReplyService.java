package com.hanaro.triptogether.tripReply.service;

import com.hanaro.triptogether.tripPlace.service.TripPlaceService;
import com.hanaro.triptogether.tripReply.domain.TripReply;
import com.hanaro.triptogether.tripReply.domain.TripReplyRepository;
import com.hanaro.triptogether.tripReply.dto.response.TripReplyResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.hanaro.triptogether.util.Constants.DELETED_MEMBER;

@Service
@RequiredArgsConstructor
public class TripReplyService {
    private final TripPlaceService tripPlaceService;
    private final TripReplyRepository tripReplyRepository;

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
}
