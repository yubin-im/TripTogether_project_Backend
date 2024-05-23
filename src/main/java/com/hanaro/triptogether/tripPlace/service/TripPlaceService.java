package com.hanaro.triptogether.tripPlace.service;

import com.hanaro.triptogether.enumeration.TeamMemberState;
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
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceOrderReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceUpdateReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.UpdateOrderReqDto;
import com.hanaro.triptogether.tripPlace.dto.response.TripPlaceResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


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

        checkTeamMember(trip.getTeam(), dto.getMember_id());

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

        checkTeamMember(tripPlace.getTrip().getTeam(), dto.getMember_id());

        tripPlace.update(place, dto.getPlace_amount(), dto.getPlace_memo(), member);
    }

    @Transactional
    public void updatePlaceOrder(Long trip_idx, UpdateOrderReqDto dto) {

        Trip trip = tripService.findByTripIdx(trip_idx);

        checkTeamMember(trip.getTeam(), dto.getMember_id());

        if(trip.getTripDay() < dto.getTrip_date()){
            throw new ApiException(ExceptionEnum.INVALID_TRIP_DATE);
        }

        List<TripPlaceOrderReqDto> dtos = dto.getOrders();
        Member member = memberService.findByMemberId(dto.getMember_id());

        for(int i=0;i<dtos.size();i++){
            TripPlace tripPlace = checkTripPlaceExists(dtos.get(i).getTrip_place_idx());
            if(!Objects.equals(tripPlace.getTrip().getTripIdx(), trip_idx)){
                throw new ApiException(ExceptionEnum.TEAM_NOT_MATCH);
            }
            tripPlace.updateOrder(i+1, member);
        }
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

    // memberId가 해당 팀원인지 확인
    private void checkTeamMember(Team dtoTeam, String member_id) {
        List<TeamMember> teamMembers = teamMemberService.findTeamMemberByMemberId(member_id);

        // 해당 멤버가 요청한 팀의 멤버인지 확인
        boolean isTeamMember = teamMembers.stream()
                .anyMatch(teamMember -> teamMember.getTeam().equals(dtoTeam));

        if (!isTeamMember) {
            throw new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER);
        }

        // 팀 멤버 상태 확인
        TeamMember teamMember = teamMembers.stream()
                .filter(tm -> tm.getTeam().equals(dtoTeam))
                .findFirst()
                .orElseThrow(() -> new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER));

        String state = teamMember.getTeamMemberState().name();
        if (state.equals(TeamMemberState.요청중.name()) || state.equals(TeamMemberState.수락대기.name())) {
            throw new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER_ROLE);
        }
    }
}
