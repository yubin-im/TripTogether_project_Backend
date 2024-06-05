package com.hanaro.triptogether.trip.service;

import com.hanaro.triptogether.city.domain.CityEntity;
import com.hanaro.triptogether.city.domain.CityRepository;
import com.hanaro.triptogether.city.dto.City;
import com.hanaro.triptogether.country.domain.CountryEntity;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.domain.MemberRepository;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.domain.TeamRepository;
import com.hanaro.triptogether.team.service.impl.TeamServiceImpl;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.trip.domain.TripRepository;
import com.hanaro.triptogether.trip.dto.request.TripReqDto;
import com.hanaro.triptogether.trip.dto.response.TripListResDto;
import com.hanaro.triptogether.trip.dto.response.TripResDto;
import com.hanaro.triptogether.tripCity.domain.TripCity;
import com.hanaro.triptogether.tripCity.domain.TripCityRepository;
import com.hanaro.triptogether.tripCity.service.TripCityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.LinkOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TeamRepository teamRepository;
    private final CityRepository cityRepository;
    private final MemberRepository memberRepository;
    private final TripCityService tripCityService;
    private final TeamServiceImpl teamService;

    public TripResDto getTrip(Long tripIdx) {
        Trip trip = findByTripIdx(tripIdx);
        return toTripResDto(trip);
    }

    public Trip findByTripIdx(Long tripIdx) {
        return tripRepository.findById(tripIdx).orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_NOT_FOUND));
    }

    public TripListResDto getTripsByTeam(Long teamIdx) {
        Team team = teamService.findTeamByTeamIdx(teamIdx); // 팀 확인
        List<Trip> trips = tripRepository.findAllByTeam_TeamIdx(teamIdx);
        List<TripResDto> dtos = new ArrayList<>();
        for (Trip trip : trips) {
            dtos.add(toTripResDto(trip));
        }

        return TripListResDto.builder().trips(dtos).preferTripIdx(team.getPreferTrip().getTripIdx()).build();
    }

    @Transactional
    public void createTrip(TripReqDto tripReqDto) throws NoSuchElementException{
        Team team = teamRepository.findById(tripReqDto.getTeamIdx())
                .orElseThrow(() -> new NoSuchElementException("Team not found"));

        Member member = memberRepository.findById(tripReqDto.getCreatedBy())
                .orElseThrow(() -> new NoSuchElementException("Member not found"));

        Trip trip = Trip.builder()
                .team(team)
                .tripName(tripReqDto.getTripName())
                .tripContent(tripReqDto.getTripContent())
                .tripGoalAmount(tripReqDto.getTripGoalAmount())
                .tripDay(tripReqDto.getTripDay())
                .tripImg(tripReqDto.getTripImg())
                .tripStartDay(tripReqDto.getTripStartDay())
                .createdBy(member)
                .build();

        tripRepository.save(trip);

        List<Long> cities = tripReqDto.getCities();
        List<TripCity> tripCities = cities.stream().map(cityId -> {
            CityEntity entity = cityRepository.findById(cityId)
                    .orElseThrow(() -> new NoSuchElementException("City not found"));

            return TripCity.builder()
                    .city(entity)
                    .trip(trip)
                    .build();
        }).collect(Collectors.toList());

        trip.setTripCities(tripCities);
    }

    @Transactional
    public void updateTrip(Long tripIdx, TripReqDto tripReqDto) throws NoSuchElementException {

        Trip trip = tripRepository.findById(tripIdx)
                .orElseThrow(() -> new NoSuchElementException("Trip not found"));

        Member member = memberRepository.findById(tripReqDto.getCreatedBy())
                .orElseThrow(() -> new NoSuchElementException("Member not found"));

        List<TripCity> newTripCities  = tripReqDto.getCities().stream().map(cityId -> {
            CityEntity entity = cityRepository.findById(cityId)
                    .orElseThrow(() -> new NoSuchElementException("City not found"));

            return TripCity.builder()
                    .city(entity)
                    .trip(trip)
                    .build();
        }).collect(Collectors.toList());

        trip.update(tripReqDto.getTripName(),
                tripReqDto.getTripContent(),
                tripReqDto.getTripGoalAmount(),
                tripReqDto.getTripDay(),
                tripReqDto.getTripImg(),
                tripReqDto.getTripStartDay(),
                newTripCities,
                member);
    }

    public void deleteTrip(Long tripIdx) throws NoSuchElementException{
        Trip trip = tripRepository.findById(tripIdx)
                .orElseThrow(() -> new NoSuchElementException("Trip not found"));

        tripRepository.delete(trip);
    }

    private TripResDto toTripResDto(Trip trip) {
        List<TripCity> tripCities = tripCityService.getTripCountry(trip.getTripIdx());
        if(tripCities.isEmpty() || tripCities.get(0).getCity()==null || tripCities.get(0).getCity().getCountry()==null) {
            return TripResDto.builder()
                    .teamIdx(trip.getTeam().getTeamIdx())
                    .teamName(trip.getTeam().getTeamName())
                    .tripIdx(trip.getTripIdx())
                    .tripDay(trip.getTripDay())
                    .tripContent(trip.getTripContent())
                    .tripGoalAmount(trip.getTripGoalAmount())
                    .tripName(trip.getTripName())
                    .tripStartDay(trip.getTripStartDay())
                    .build();
        }
        List<City> cities = tripCities.stream().map(tripCity -> tripCity.getCity().toCity()).toList();
        CountryEntity country = tripCities.get(0).getCity().getCountry();
        return TripResDto.builder()
                .teamIdx(trip.getTeam().getTeamIdx())
                .teamName(trip.getTeam().getTeamName())
                .tripIdx(trip.getTripIdx())
                .tripDay(trip.getTripDay())
                .tripContent(trip.getTripContent())
                .tripGoalAmount(trip.getTripGoalAmount())
                .tripName(trip.getTripName())
                .tripStartDay(trip.getTripStartDay())
                .countryIdx(country.getCountryIdx())
                .countryNameEng(country.getCountryNameEng())
                .countryNameKo(country.getCountryNameKo())
                .cities(cities)
                .build();
    }

    @Transactional
    public void setGoalAmount(Long tripIdx, BigDecimal goalAmount) {
        Trip trip = tripRepository.findById(tripIdx)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_NOT_FOUND));
        tripRepository.updateGoalAmount(tripIdx, goalAmount);
    }
}
