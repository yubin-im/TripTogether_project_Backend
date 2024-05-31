package com.hanaro.triptogether.trip.service;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.city.domain.CityEntity;
import com.hanaro.triptogether.country.domain.CountryEntity;
import com.hanaro.triptogether.enumeration.PreferenceType;
import com.hanaro.triptogether.enumeration.TeamType;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.team.service.impl.TeamServiceImpl;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.trip.domain.TripRepository;
import com.hanaro.triptogether.trip.dto.response.TripResDto;
import com.hanaro.triptogether.tripCity.domain.TripCity;
import com.hanaro.triptogether.tripCity.service.TripCityService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TripServiceTest {
    @Mock
    private TripRepository tripRepository;

    @Mock
    private TripCityService tripCityService;

    @Mock
    private TeamServiceImpl teamService;

    @Spy
    @InjectMocks
    private TripService tripService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static Long tripIdx;
    private static Trip trip;
    private static Trip trip2;
    private static Team team;
    private static Member member1;
    private static CountryEntity country;
    private static CityEntity city;
    private static TripCity tripCity;

    @BeforeAll
    static void init() {
        tripIdx = 1L;
        member1 = Member.builder()
                .memberIdx(1L)
                .memberId("member1")
                .memberPw("123456")
                .memberLoginPw("123456")
                .alarmStatus(true)
                .memberName("memberName1")
                .createdAt(LocalDateTime.now())
                .build();

        team = Team.builder()
                .teamIdx(1L)
                .teamName("teamName")
                .account(mock(Account.class))
                .teamType(TeamType.여행)
                .preferenceType(PreferenceType.모두)
                .teamNotice("지각비 5만원")
                .createdBy(member1)
                .build();
        country = CountryEntity.builder()
                .countryIdx(1L)
                .build();
        city = CityEntity.builder()
                .country(country).build();
        tripCity = TripCity.builder()
                .city(city).build();
        trip = Trip.builder()
                .tripIdx(1L)
                .team(team)
                .tripName("tripName")
                .tripContent("tripContent")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .createdBy(member1)
                .tripCities(List.of(tripCity))
                .build();
        trip2 = Trip.builder()
                .tripIdx(2L)
                .team(team)
                .tripName("tripName2")
                .tripContent("tripContent2")
                .tripGoalAmount(BigDecimal.valueOf(100))
                .tripDay(3)
                .tripStartDay(LocalDate.of(2025, 1, 1))
                .createdAt(LocalDateTime.now())
                .createdBy(member1)
                .tripCities(List.of(tripCity))
                .build();
    }
    @Test
    void findByTripIdx_success() {
        //given
        given(tripRepository.findById(tripIdx)).willReturn(Optional.of(trip));

        //when
        Trip trip = tripService.findByTripIdx(tripIdx);

        //then
        assertEquals(tripIdx, trip.getTripIdx());
    }
    @Test
    void findByTripIdx_invalidTrip() {
        //given
        given(tripRepository.findById(tripIdx)).willReturn(Optional.empty());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripService.findByTripIdx(tripIdx));

        // then
        assertEquals(ExceptionEnum.TRIP_NOT_FOUND.getMessage(), exception.getError().getMessage());
    }

    @Test
    void getTripsByTeam_success() {
        //given
        given(tripRepository.findAllByTeam_TeamIdx(trip.getTeam().getTeamIdx())).willReturn(List.of(trip, trip2));
        given(tripCityService.getTripCountry(trip.getTripIdx())).willReturn(trip.getTripCities());
        given(tripCityService.getTripCountry(trip2.getTripIdx())).willReturn(trip2.getTripCities());

        //when
        List<TripResDto> dtos = tripService.getTripsByTeam(trip.getTeam().getTeamIdx());


        //then
        assertEquals(2, dtos.size());
        assertEquals(trip.getTripIdx(), dtos.get(0).getTripIdx());
        assertEquals(trip2.getTripIdx(), dtos.get(1).getTripIdx());
    }
    @Test
    void getTripsByTeam_invalidTeamIdx() {
        //given
        given(teamService.findTeamByTeamIdx(anyLong())).willThrow(new ApiException(ExceptionEnum.TEAM_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripService.getTripsByTeam(anyLong()));

        // then
        assertEquals(ExceptionEnum.TEAM_NOT_FOUND.getMessage(), exception.getError().getMessage());
    }
}