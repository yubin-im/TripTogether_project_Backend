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
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceOrderReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceUpdateReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.UpdateOrderReqDto;
import com.hanaro.triptogether.tripPlace.dto.response.TripPlaceResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;


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
        validateTeamMember(trip.getTeam(), dto.getMember_id());
        validateTripDate(trip, dto.getTrip_date());

        Place place = placeService.findByPlaceIdx(dto.getPlace_idx());
        Member member = memberService.findByMemberId(dto.getMember_id());
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
        validateTeamMember(tripPlace.getTrip().getTeam(), dto.getMember_id());

        Place place = placeService.findByPlaceIdx(dto.getPlace_idx());
        Member member = memberService.findByMemberId(dto.getMember_id());
        tripPlace.update(place, dto.getPlace_amount(), dto.getPlace_memo(), member);
    }

    @Transactional
    public void updatePlaceOrder(Long trip_idx, UpdateOrderReqDto dto) {

        Trip trip = tripService.findByTripIdx(trip_idx);

        validateTeamMember(trip.getTeam(), dto.getMember_id());
        validateTripDate(trip, dto.getTrip_date());

        List<TripPlaceOrderReqDto> dtos = dto.getOrders();
        Member member = memberService.findByMemberId(dto.getMember_id());
        int num = tripPlaceRepository.countByTripId(trip_idx, dto.getTrip_date());
        if (dtos.stream().map(TripPlaceOrderReqDto::getTrip_place_idx).distinct().count() != num){ //중복 및 사이즈 체크
            throw new ApiException(ExceptionEnum.INVALID_ORDER_LIST);
        }
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
        TripPlace tripPlaceToDelete = checkTripPlaceExists(trip_place_idx);
        Integer deletedPlaceOrder = tripPlaceToDelete.getPlaceOrder();
        Long tripId = tripPlaceToDelete.getTrip().getTripIdx();

        // 삭제할 일정 삭제
        tripPlaceRepository.deleteById(trip_place_idx);

        // placeOrder 감소
        tripPlaceRepository.decrementPlaceOrderAfter(tripId, deletedPlaceOrder);
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

    private void validateTeamMember(Team team, String member_id) {
        //내 팀 리스트 리턴
        List<TeamMember> teamMembers = teamMemberService.findTeamMemberByMemberId(member_id);
        //팀 멤버 여부 확인
        TeamMember teamMember = teamMemberService.checkIsMyTeam(team, teamMembers);
        //유효한 상태인지 확인
        teamMemberService.validateTeamMemberState(teamMember);
    }

    private void validateTripDate(Trip trip, int trip_date) {
        //여행 일자 범위 확인
        if (trip.getTripDay() < trip_date) {
            throw new ApiException(ExceptionEnum.INVALID_TRIP_DATE);
        }
    }

}
