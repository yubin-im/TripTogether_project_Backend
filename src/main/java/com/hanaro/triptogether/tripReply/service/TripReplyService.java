package com.hanaro.triptogether.tripReply.service;

import com.hanaro.triptogether.tripPlace.service.TripPlaceService;
import com.hanaro.triptogether.tripReply.domain.TripReplyRepository;
import com.hanaro.triptogether.tripReply.dto.response.TripReplyResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripReplyService {
    private final TripPlaceService tripPlaceService;
    private final TripReplyRepository tripReplyRepository;

    public List<TripReplyResDto> getReply(Long trip_place_idx) {
        tripPlaceService.checkTripPlaceExists(trip_place_idx);
        return tripReplyRepository.findAllByTripPlace_TripPlaceIdxOrderByCreatedAtAsc(trip_place_idx).stream().map(TripReplyResDto::new).toList();
    }
}
