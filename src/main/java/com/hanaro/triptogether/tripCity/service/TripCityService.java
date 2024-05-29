package com.hanaro.triptogether.tripCity.service;

import com.hanaro.triptogether.country.domain.CountryEntity;
import com.hanaro.triptogether.tripCity.domain.TripCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TripCityService {
    private final TripCityRepository tripCityRepository;

    public CountryEntity getTripCountry(Long tripIdx) {
        return tripCityRepository.findAllByTrip_TripIdx(tripIdx).get(0).getCity().getCountry();
    }
}
