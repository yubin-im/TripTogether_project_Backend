package com.hanaro.triptogether.tripPlace.service;

import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import com.hanaro.triptogether.tripPlace.domain.TripPlaceRepository;
import com.hanaro.triptogether.tripPlace.dto.response.TripPlaceResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TripPlaceService {
    private final TripPlaceRepository tripPlaceRepository;

    public List<TripPlaceResDto> getPlace(Long trip_idx) {
        List<TripPlace> tripPlaces = tripPlaceRepository.findAllByTrip_TripIdxOrderByTripDateAscPlaceOrderAsc(trip_idx);
        return tripPlaces.stream().map(TripPlaceResDto::new).toList();
    }
}
