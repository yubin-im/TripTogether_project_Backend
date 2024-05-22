package com.hanaro.triptogether.tripPlace.service;

import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.service.impl.MemberServiceImpl;
import com.hanaro.triptogether.place.domain.Place;
import com.hanaro.triptogether.place.service.PlaceService;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.teamMember.service.impl.TeamMemberServiceImpl;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.trip.service.TripService;
import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import com.hanaro.triptogether.tripPlace.domain.TripPlaceRepository;
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceAddReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceUpdateReqDto;
import com.hanaro.triptogether.tripPlace.dto.response.TripPlaceResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TripPlaceService {
    private final TripService tripService;
    private final PlaceService placeService;
    private final TeamMemberServiceImpl teamMemberService;
    private final MemberServiceImpl memberService;
    private final TripPlaceRepository tripPlaceRepository;

    @Transactional
    public void addPlace(TripPlaceAddReqDto dto) {

        Trip trip =  tripService.findByTripIdx(dto.getTrip_idx());
        Place place = placeService.findByPlaceIdx(dto.getPlace_idx());
        Member member = memberService.findByMemberId(dto.getMember_id());

        // 요청자가 해당 여행 팀원인지 확인
        List<Team> teams = teamMemberService.findTeamMemberByMemberId(dto.getMember_id())
                .stream()
                .map(TeamMember::getTeam)
                .toList();

        boolean isTeamMember = teams.stream()
                .anyMatch(team -> team.equals(trip.getTeam()));

        if (!isTeamMember) {
            throw new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER);
        }

        //여행 일자 범위 확인
        if(trip.getTripDay() < dto.getTrip_date()){
            throw new ApiException(ExceptionEnum.INVALID_TRIP_DATE);
        }

        int placeOrder = tripPlaceRepository.countByTripId(dto.getTrip_idx(), dto.getTrip_date())+ 1;

        TripPlace tripPlace = TripPlace.builder()
                .trip(trip)
                .tripDate(dto.getTrip_date())
                .placeOrder(placeOrder)
                .place(place)
                .placeAmount(dto.getPlace_amount())
                .placeMemo(dto.getPlace_memo())
                .member(member)
                .build();
        tripPlaceRepository.save(tripPlace);
    }

    @Transactional
    public void updatePlace(Long trip_place_idx, TripPlaceUpdateReqDto dto) {
        TripPlace tripPlace = checkTripPlaceExists(trip_place_idx);
        Place place = placeService.findByPlaceIdx(dto.getPlace_idx());
        Member member = memberService.findByMemberId(dto.getMember_id());

        // 요청자가 해당 여행 팀원인지 확인
        List<Team> teams = teamMemberService.findTeamMemberByMemberId(dto.getMember_id())
                .stream()
                .map(TeamMember::getTeam)
                .toList();

        boolean isTeamMember = teams.stream()
                .anyMatch(team -> team.equals(tripPlace.getTrip().getTeam()));
        if (!isTeamMember) {
            throw new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER);
        }
        tripPlace.update(place, dto.getPlace_amount(), dto.getPlace_memo(), member);

    }


    public List<TripPlaceResDto> getPlace(Long trip_idx) {
        List<TripPlace> tripPlaces = tripPlaceRepository.findAllByTrip_TripIdxOrderByTripDateAscPlaceOrderAsc(trip_idx);
        return tripPlaces.stream().map(TripPlaceResDto::new).toList();
    }

    @Transactional
    public void deleteTripPlace(Long trip_place_idx) {
        checkTripPlaceExists(trip_place_idx);
        tripPlaceRepository.deleteById(trip_place_idx);
    }

    public TripPlace checkTripPlaceExists(Long trip_place_idx){
        return tripPlaceRepository.findById(trip_place_idx).orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));
    }

    public Long findTeamIdByTripPlaceIdx(Long trip_place_idx){
        return tripPlaceRepository
                .findById(trip_place_idx)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND))
                .getTrip()
                .getTeam()
                .getTeamIdx();
    }
}
