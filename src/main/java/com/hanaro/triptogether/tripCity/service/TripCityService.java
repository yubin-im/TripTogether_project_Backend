package com.hanaro.triptogether.tripCity.service;

import com.hanaro.triptogether.tripCity.domain.TripCity;
import com.hanaro.triptogether.tripCity.domain.TripCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TripCityService {
    private final TripCityRepository tripCityRepository;

    public List<TripCity> getTripCountry(Long tripIdx) {
        return tripCityRepository.findAllByTrip_TripIdx(tripIdx);
    }
}
