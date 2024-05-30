package com.hanaro.triptogether.trip.service;

import com.hanaro.triptogether.city.dto.City;
import com.hanaro.triptogether.country.domain.CountryEntity;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.service.impl.TeamServiceImpl;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.trip.domain.TripRepository;
import com.hanaro.triptogether.trip.dto.response.TripResDto;
import com.hanaro.triptogether.tripCity.domain.TripCity;
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
    private final TeamServiceImpl teamService;

    public TripResDto getTrip(Long tripIdx) {
        Trip trip = findByTripIdx(tripIdx);
        return toTripResDto(trip);
    }

    public Trip findByTripIdx(Long tripIdx) {
        return tripRepository.findById(tripIdx).orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_NOT_FOUND));
    }

    public List<TripResDto> getTripsByTeam(Long teamIdx) {
        teamService.findTeamByTeamIdx(teamIdx); // 팀 확인
        List<Trip> trips = tripRepository.findAllByTeam_TeamIdx(teamIdx);
        List<TripResDto> dtos = new ArrayList<>();
        for (Trip trip : trips) {
            dtos.add(toTripResDto(trip));
        }
        return dtos;
    }

    private TripResDto toTripResDto(Trip trip) {
        List<TripCity> tripCities = tripCityService.getTripCountry(trip.getTripIdx());
        List<City> cities = tripCities.stream().map(tripCity -> tripCity.getCity().toCity()).toList();
        return TripResDto.builder()
                .teamIdx(trip.getTeam().getTeamIdx())
                .teamName(trip.getTeam().getTeamName())
                .tripIdx(trip.getTripIdx())
                .tripDay(trip.getTripDay())
                .tripContent(trip.getTripContent())
                .tripGoalAmount(trip.getTripGoalAmount())
                .tripName(trip.getTripName())
                .tripStartDay(trip.getTripStartDay())
                .cities(cities)
                .build();
    }
}
