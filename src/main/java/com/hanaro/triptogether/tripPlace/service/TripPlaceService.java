package com.hanaro.triptogether.tripPlace.service;

import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.service.impl.MemberServiceImpl;
import com.hanaro.triptogether.place.domain.PlaceEntity;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


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
        Trip trip =  tripService.findByTripIdx(dto.getTripIdx());
        validateTeamMember(trip.getTeam(), dto.getMemberIdx());
        validateTripDate(trip, dto.getTripDate());

        PlaceEntity place = placeService.findByPlaceIdx(dto.getPlaceIdx());
        Member member = memberService.findByMemberIdx(dto.getMemberIdx());
        int placeOrder = tripPlaceRepository.countByTripId(dto.getTripIdx(), dto.getTripDate())+ 1;

        TripPlace tripPlace = TripPlace.builder()
                .trip(trip)
                .tripDate(dto.getTripDate())
                .placeOrder(placeOrder)
                .place(place)
                .placeAmount(dto.getPlaceAmount())
                .placeMemo(dto.getPlaceMemo())
                .createdAt(LocalDateTime.now())
                .createdBy(member)
                .build();
        tripPlaceRepository.save(tripPlace);
    }

    @Transactional
    public void updatePlace(Long trip_placeIdx, TripPlaceUpdateReqDto dto) {
        TripPlace tripPlace = checkTripPlaceExists(trip_placeIdx);
        validateTeamMember(tripPlace.getTrip().getTeam(), dto.getMemberIdx());

        PlaceEntity place = placeService.findByPlaceIdx(dto.getPlaceIdx());
        Member member = memberService.findByMemberIdx(dto.getMemberIdx());
        tripPlace.update(place, dto.getPlaceAmount(), dto.getPlaceMemo(), member);
    }

    @Transactional
    public void updatePlaceOrder(Long tripIdx, UpdateOrderReqDto dto) {

        Trip trip = tripService.findByTripIdx(tripIdx);

        validateTeamMember(trip.getTeam(), dto.getMemberIdx());
        validateTripDate(trip, dto.getTripDate());

        List<TripPlaceOrderReqDto> dtos = dto.getOrders();
        Member member = memberService.findByMemberIdx(dto.getMemberIdx());
        int num = tripPlaceRepository.countByTripId(tripIdx, dto.getTripDate());
        if (dtos.stream().map(TripPlaceOrderReqDto::getTripPlaceIdx).distinct().count() != num){ //중복 및 사이즈 체크
            throw new ApiException(ExceptionEnum.INVALID_ORDER_LIST);
        }
        for(int i=0;i<dtos.size();i++){
            TripPlace tripPlace = checkTripPlaceExists(dtos.get(i).getTripPlaceIdx());
            if(!Objects.equals(tripPlace.getTrip().getTripIdx(), tripIdx)){
                throw new ApiException(ExceptionEnum.TEAM_NOT_MATCH);
            }
            tripPlace.updateOrder(i+1, member);
        }
    }


    public List<TripPlaceResDto> getPlace(Long tripIdx) {
        List<TripPlace> tripPlaces = tripPlaceRepository.findAllByTrip_TripIdxOrderByTripDateAscPlaceOrderAsc(tripIdx);
        return tripPlaces.stream().map(TripPlaceResDto::new).toList();
    }

    @Transactional
    public void deleteTripPlace(Long trip_placeIdx) {
        TripPlace tripPlaceToDelete = checkTripPlaceExists(trip_placeIdx);
        Integer deletedPlaceOrder = tripPlaceToDelete.getPlaceOrder();
        Long tripId = tripPlaceToDelete.getTrip().getTripIdx();

        // 삭제할 일정 삭제
        tripPlaceRepository.deleteById(trip_placeIdx);

        // placeOrder 감소
        tripPlaceRepository.decrementPlaceOrderAfter(tripId, deletedPlaceOrder);
    }

    public TripPlace checkTripPlaceExists(Long trip_placeIdx){
        return tripPlaceRepository.findById(trip_placeIdx).orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));
    }

    public Long findTeamIdByTripPlaceIdx(Long trip_placeIdx){
        return tripPlaceRepository
                .findById(trip_placeIdx)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND))
                .getTrip()
                .getTeam()
                .getTeamIdx();
    }

    void validateTeamMember(Team team, Long memberIdx) {
        //내 팀 리스트 리턴
        List<TeamMember> teamMembers = teamMemberService.findTeamMemberByMemberIdx(memberIdx);
        //팀 멤버 여부 확인
        TeamMember teamMember = teamMemberService.checkIsMyTeam(team, teamMembers);
        //유효한 상태인지 확인
        teamMemberService.validateTeamMemberState(teamMember);
    }

    void validateTripDate(Trip trip, int tripDate) {
        //여행 일자 범위 확인
        if (trip.getTripDay() < tripDate) {
            throw new ApiException(ExceptionEnum.INVALID_TRIP_DATE);
        }
    }

}
