package com.hanaro.triptogether.trip.service;

import com.hanaro.triptogether.country.domain.CountryEntity;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.trip.domain.TripRepository;
import com.hanaro.triptogether.trip.dto.response.TripResDto;
import com.hanaro.triptogether.tripCity.service.TripCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripCityService tripCityService;

    public Trip findByTripIdx(Long tripIdx) {
        return tripRepository.findById(tripIdx).orElseThrow(()->new ApiException(ExceptionEnum.TRIP_NOT_FOUND));
    }

    public List<TripResDto> getTripsByTeam(Long teamIdx) {

        List<Trip> trips = tripRepository.findAllByTeam_TeamIdx(teamIdx);
        List<TripResDto> dtos = new ArrayList<>();
        for(Trip trip:trips){
            Long tripIdx = trip.getTripIdx();
            CountryEntity country = tripCityService.getTripCountry(tripIdx);
            dtos.add(TripResDto.builder()
                    .tripIdx(tripIdx)
                    .tripDay(trip.getTripDay())
                    .tripContent(trip.getTripContent())
                    .tripGoalAmount(trip.getTripGoalAmount())
                    .tripName(trip.getTripName())
                    .tripStartDay(trip.getTripStartDay())
                    .countryIdx(country.getCountryIdx())
                    .countryNameKo(country.getCountryNameKo())
                    .countryNameEng(country.getCountryNameEng())
                    .build());
        }
        return dtos;
    }
}
