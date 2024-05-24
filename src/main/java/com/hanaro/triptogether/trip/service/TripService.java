package com.hanaro.triptogether.trip.service;

import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.trip.domain.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;

    public Trip findByTripIdx(Long tripIdx) {
        return tripRepository.findById(tripIdx).orElseThrow(()->new ApiException(ExceptionEnum.TRIP_NOT_FOUND));
    }

}
