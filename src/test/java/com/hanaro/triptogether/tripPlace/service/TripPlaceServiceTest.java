package com.hanaro.triptogether.tripPlace.service;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.city.domain.CityEntity;
import com.hanaro.triptogether.country.domain.CountryEntity;
import com.hanaro.triptogether.enumeration.PreferenceType;
import com.hanaro.triptogether.enumeration.TeamType;
import com.hanaro.triptogether.exception.ApiException;
import com.hanaro.triptogether.exception.ExceptionEnum;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.member.service.impl.MemberServiceImpl;
import com.hanaro.triptogether.place.domain.PlaceEntity;
import com.hanaro.triptogether.place.service.PlaceService;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.teamMember.service.impl.TeamMemberServiceImpl;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.trip.service.TripService;
import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import com.hanaro.triptogether.tripPlace.domain.TripPlaceRepository;
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceAddReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceOrderReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceUpdateInfoReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.UpdateOrderReqDto;
import com.hanaro.triptogether.tripPlace.dto.response.TripPlaceResDto;
import com.hanaro.triptogether.tripReply.domain.TripReply;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class TripPlaceServiceTest {
    @Mock
    private TripPlaceRepository tripPlaceRepository;

    @Mock
    private TripService tripService;

    @Mock
    private PlaceService placeService;

    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private TeamMemberServiceImpl teamMemberService;

    @Mock
    private TripPlace tripPlace;

    @Spy
    @InjectMocks
    private TripPlaceService tripPlaceService;

    private static Trip trip;
    private static Team team;
    private static PlaceEntity place;
    private static Member member1;
    private static Member member2;

    private static Account account;
    private static CityEntity city;
    private static CountryEntity country;

    @BeforeAll
    static void beforeAll() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        member1 = Member.builder()
                .memberIdx(1L)
                .memberId("member1")
                .memberPw("123456")
                .memberLoginPw("123456")
                .alarmStatus(true)
                .memberName("memberName1")
                .createdAt(LocalDateTime.now())
                .build();

        member2 = Member.builder()
                .memberIdx(2L)
                .memberId("member2")
                .memberPw("123456")
                .memberLoginPw("123456")
                .alarmStatus(true)
                .memberName("memberName2")
                .createdAt(LocalDateTime.now())
                .build();

        country = CountryEntity.builder()
                .countryIdx(1L)
                .countryNameEng("countryNameEng")
                .countryNameKo("나라명")
                .createdAt(LocalDateTime.now())
                .createdBy(1L)
                .build();

        team = Team.builder()
                .teamIdx(1L)
                .teamName("teamName")
                .account(account)
                .teamType(TeamType.여행)
                .preferenceType(PreferenceType.모두)
                .teamNotice("지각비 5만원")
                .createdBy(member1)
                .build();


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
                .build();

        city = CityEntity.builder()
                .cityIdx(1L)
                .country(country)
                .cityNameKo("cityNameKo")
                .createdAt(LocalDateTime.now())
                .createdBy(member1)
                .build();

        place = PlaceEntity.builder()
                .placeIdx(1L)
                .city(city)
                .placeNameKo("placeNameKo")
                .createdAt(LocalDateTime.now())
                .createdBy(member1)
                .build();

    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPlace_TripNotFound() {
        // given
        TripPlaceAddReqDto tripPlaceAddReqDto = TripPlaceAddReqDto.builder()
                .tripIdx(1L)
                .memberIdx(1L)
                .tripDate(1)
                .placeIdx(1L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTripIdx())).willThrow(new ApiException(ExceptionEnum.TRIP_NOT_FOUND));

        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.addPlace(tripPlaceAddReqDto));

        assertEquals( ExceptionEnum.TRIP_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripPlaceRepository).should(never()).save(any(TripPlace.class));
    }

    @Test
    void addPlace_InvalidTeamMember() {
        // given
        TripPlaceAddReqDto tripPlaceAddReqDto = TripPlaceAddReqDto.builder()
                .tripIdx(1L)
                .memberIdx(1L)
                .tripDate(1)
                .placeIdx(1L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTripIdx())).willReturn(trip);
        willThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER))
                .given(teamMemberService).findTeamMemberByMemberIdx(anyLong());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.addPlace(tripPlaceAddReqDto));

        // then
        assertEquals(ExceptionEnum.INVALID_TEAM_MEMBER.getMessage(), exception.getError().getMessage());
        then(tripPlaceRepository).should(never()).save(any(TripPlace.class));
    }

    @Test
    void addPlace_InvalidTripDate() {
        // given
        TripPlaceAddReqDto tripPlaceAddReqDto = TripPlaceAddReqDto.builder()
                .tripIdx(1L)
                .memberIdx(1L)
                .tripDate(4) // tripDate가 tripDay보다 큼
                .placeIdx(1L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTripIdx())).willReturn(trip);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.addPlace(tripPlaceAddReqDto));

        // then
        assertEquals(ExceptionEnum.INVALID_TRIP_DATE.getMessage(), exception.getError().getMessage());
        then(tripPlaceRepository).should(never()).save(any(TripPlace.class));
    }

    @Test
    void addPlace_PlaceNotFound() {
        // given
        TripPlaceAddReqDto tripPlaceAddReqDto = TripPlaceAddReqDto.builder()
                .tripIdx(1L)
                .memberIdx(1L)
                .tripDate(1)
                .placeIdx(1L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTripIdx())).willReturn(trip);
        given(placeService.findByPlaceIdx(tripPlaceAddReqDto.getPlaceIdx()))
                .willThrow(new ApiException(ExceptionEnum.PLACE_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.addPlace(tripPlaceAddReqDto));

        // then
        assertEquals(ExceptionEnum.PLACE_NOT_FOUND.getMessage(), exception.getError().getMessage());
        then(tripPlaceRepository).should(never()).save(any(TripPlace.class));
    }

    @Test
    void addPlace_MemberNotFound() {
        // given
        TripPlaceAddReqDto tripPlaceAddReqDto = TripPlaceAddReqDto.builder()
                .tripIdx(1L)
                .memberIdx(1L)
                .tripDate(1)
                .placeIdx(1L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTripIdx())).willReturn(trip);
        given(placeService.findByPlaceIdx(tripPlaceAddReqDto.getPlaceIdx())).willReturn(place);
        given(memberService.findByMemberIdx(tripPlaceAddReqDto.getMemberIdx()))
                .willThrow(new ApiException(ExceptionEnum.MEMBER_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.addPlace(tripPlaceAddReqDto));

        // then
        assertEquals(ExceptionEnum.MEMBER_NOT_FOUND.getMessage(), exception.getError().getMessage());
        then(tripPlaceRepository).should(never()).save(any(TripPlace.class));
    }

    @Test
    void addPlace_Success() {
        // given
        TripPlaceAddReqDto tripPlaceAddReqDto = TripPlaceAddReqDto.builder()
                .tripIdx(1L)
                .memberIdx(1L)
                .tripDate(1)
                .placeIdx(1L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTripIdx())).willReturn(trip);
        given(placeService.findByPlaceIdx(tripPlaceAddReqDto.getPlaceIdx())).willReturn(place);
        given(memberService.findByMemberIdx(tripPlaceAddReqDto.getMemberIdx())).willReturn(member1);
        given(tripPlaceRepository.countByTripIdAndTripDate(tripPlaceAddReqDto.getTripIdx(), tripPlaceAddReqDto.getTripDate()))
                .willReturn(0);

        // when
        tripPlaceService.addPlace(tripPlaceAddReqDto);

        // then
        then(tripService).should(times(1)).findByTripIdx(tripPlaceAddReqDto.getTripIdx());
        then(placeService).should(times(1)).findByPlaceIdx(tripPlaceAddReqDto.getPlaceIdx());
        then(memberService).should(times(1)).findByMemberIdx(tripPlaceAddReqDto.getMemberIdx());
        then(tripPlaceRepository).should(times(1)).save(any(TripPlace.class));

        ArgumentCaptor<TripPlace> tripPlaceCaptor = ArgumentCaptor.forClass(TripPlace.class);
        then(tripPlaceRepository).should().save(tripPlaceCaptor.capture());
        TripPlace capturedTripPlace = tripPlaceCaptor.getValue();

        assertEquals(trip, capturedTripPlace.getTrip());
        assertEquals(tripPlaceAddReqDto.getTripDate(), capturedTripPlace.getTripDate());
        assertEquals(1, capturedTripPlace.getPlaceOrder());
        assertEquals(place, capturedTripPlace.getPlace());
        assertEquals(tripPlaceAddReqDto.getPlaceAmount(), capturedTripPlace.getPlaceAmount());
        assertEquals(tripPlaceAddReqDto.getPlaceMemo(), capturedTripPlace.getPlaceMemo());
        assertEquals(member1, capturedTripPlace.getCreatedBy());
    }

    private TripPlace createMockTripPlace(Long tripPlaceIdx) {
        return TripPlace.builder()
                .tripPlaceIdx(tripPlaceIdx)
                .trip(trip)
                .place(place)
                .placeAmount(BigDecimal.valueOf(50)) // 임의의 초기 값 설정
                .placeMemo("Previous Memo") // 임의의 초기 값 설정
                .createdBy(member1)
                .tripReplies(List.of(mock(TripReply.class)))
                .build();
    }

    @Test
    void updatePlace_Success() {
        // given
        Long tripPlaceIdx = 1L; // 수정할 일정 idx
        TripPlaceUpdateInfoReqDto dto = TripPlaceUpdateInfoReqDto.builder()
                .placeIdx(2L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .memberIdx(2L)
                .build();

        TripPlace tripPlace = createMockTripPlace(tripPlaceIdx); // 가짜 TripPlace 객체 생성

        given(tripPlaceRepository.findById(tripPlaceIdx)).willReturn(Optional.of(tripPlace));
        given(placeService.findByPlaceIdx(dto.getPlaceIdx())).willReturn(
                PlaceEntity.builder()
                .placeIdx(2L)
                .city(city)
                .placeNameKo("placeNameKo")
                .createdAt(LocalDateTime.now())
                .createdBy(member1)
                .build());
        given(memberService.findByMemberIdx(dto.getMemberIdx())).willReturn(member2);

        // when
        tripPlaceService.updatePlace(tripPlaceIdx, dto);

        // then
        // 수정된 값을 다시 DB에서 가져와서 확인
        TripPlace updatedTripPlace = tripPlaceRepository.findById(tripPlaceIdx).orElse(null);
        assertNotNull(updatedTripPlace);
        assertEquals(dto.getPlaceIdx(), updatedTripPlace.getPlace().toPlace().getPlaceIdx());
        assertEquals(dto.getPlaceAmount(), updatedTripPlace.getPlaceAmount());
        assertEquals(dto.getPlaceMemo(), updatedTripPlace.getPlaceMemo());
        assertEquals(dto.getMemberIdx(), updatedTripPlace.getLastModifiedBy().getMemberIdx());
    }

    @Test
    void updatePlace_tripPlaceNotFound() {
        // given
        Long tripPlaceIdx = 100L; // 없는 tripPlaceIdx
        TripPlaceUpdateInfoReqDto dto = TripPlaceUpdateInfoReqDto.builder()
                .placeIdx(1L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .memberIdx(1L)
                .build();

        given(tripPlaceRepository.findById(tripPlaceIdx)).willThrow(new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlace(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.TRIP_PLACE_NOT_FOUND.getMessage(), exception.getError().getMessage());
    }
    @Test
    void updatePlace_memberNotInTeam() {
        // given
        Long tripPlaceIdx = 1L; // 수정할 일정 idx
        TripPlaceUpdateInfoReqDto dto = TripPlaceUpdateInfoReqDto.builder()
                .placeIdx(2L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .memberIdx(1L)
                .build();

        TripPlace tripPlace = createMockTripPlace(tripPlaceIdx); // 가짜 TripPlace 객체 생성

        given(tripPlaceRepository.findById(tripPlaceIdx)).willReturn(Optional.of(tripPlace));
        given(memberService.findByMemberIdx(dto.getMemberIdx())).willReturn(member2); // `member2`가 팀의 멤버가 아님을 가정

        doThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER)).when(tripPlaceService).validateTeamMember(any(Team.class), eq(dto.getMemberIdx()));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlace(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.INVALID_TEAM_MEMBER.getMessage(), exception.getError().getMessage());
    }

    @Test
    void updatePlace_placeNotFound() {
        // given
        Long tripPlaceIdx = 1L; // 수정할 일정 idx
        TripPlaceUpdateInfoReqDto dto = TripPlaceUpdateInfoReqDto.builder()
                .placeIdx(2L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .memberIdx(2L)
                .build();

        TripPlace tripPlace = createMockTripPlace(tripPlaceIdx); // 가짜 TripPlace 객체 생성

        given(tripPlaceRepository.findById(tripPlaceIdx)).willReturn(Optional.of(tripPlace));
        given(placeService.findByPlaceIdx(dto.getPlaceIdx())).willThrow(new ApiException(ExceptionEnum.PLACE_NOT_FOUND));
        given(memberService.findByMemberIdx(dto.getMemberIdx())).willReturn(member2);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlace(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.PLACE_NOT_FOUND.getMessage(), exception.getError().getMessage());
    }

    @Test
    void updatePlace_memberNotFound() {
        // given
        Long tripPlaceIdx = 1L; // 수정할 일정 idx
        TripPlaceUpdateInfoReqDto dto = TripPlaceUpdateInfoReqDto.builder()
                .placeIdx(2L)
                .placeAmount(BigDecimal.valueOf(100))
                .placeMemo("Memo")
                .memberIdx(10L)
                .build();

        TripPlace tripPlace = createMockTripPlace(tripPlaceIdx); // 가짜 TripPlace 객체 생성

        given(tripPlaceRepository.findById(tripPlaceIdx)).willReturn(Optional.of(tripPlace));
        given(placeService.findByPlaceIdx(dto.getPlaceIdx())).willReturn(
                PlaceEntity.builder()
                        .placeIdx(2L)
                        .city(city)
                        .placeNameKo("placeNameKo")
                        .createdAt(LocalDateTime.now())
                        .createdBy(member1)
                        .build());
        given(memberService.findByMemberIdx(dto.getMemberIdx())).willThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlace(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.INVALID_TEAM_MEMBER.getMessage(), exception.getError().getMessage());
    }


    private TripPlace createMockTripPlace(Long tripPlaceIdx, int placeOrder) {
        return TripPlace.builder()
                .tripPlaceIdx(tripPlaceIdx)
                .trip(trip)
                .place(place)
                .placeOrder(placeOrder)
                .createdBy(member1)
                .build();
    }

    @Test
    void updatePlaceOrder_success() {
        // given
        Long tripIdx = 1L;
        Long tripPlaceIdx1 = 1L;
        Long tripPlaceIdx2 = 2L;
        int tripDate = 1;

        TripPlaceOrderReqDto orderDto1 = TripPlaceOrderReqDto.builder().tripPlaceIdx(tripPlaceIdx1).placeOrder(1).tripDate(1).build();
        TripPlaceOrderReqDto orderDto2 = TripPlaceOrderReqDto.builder().tripPlaceIdx(tripPlaceIdx2).placeOrder(2).tripDate(1).build();
        List<TripPlaceOrderReqDto> orders = List.of(orderDto1, orderDto2);
        UpdateOrderReqDto updateOrderReqDto = UpdateOrderReqDto.builder()
                .memberIdx(1L)
                .orders(orders)
                .build();

        TripPlace tripPlace1 = createMockTripPlace(tripPlaceIdx1, 1);
        TripPlace tripPlace2 = createMockTripPlace(tripPlaceIdx2, 2);

        given(tripService.findByTripIdx(tripIdx)).willReturn(trip);
        willDoNothing().given(tripPlaceService).validateTeamMember(any(),anyLong());
        willDoNothing().given(tripPlaceService).validateTripDate(any(), anyInt());
        given(memberService.findByMemberIdx(1L)).willReturn(member1);
        given(tripPlaceRepository.countByTripId(tripIdx)).willReturn(2);
        given(tripPlaceRepository.findById(tripPlaceIdx1)).willReturn(Optional.of(tripPlace1));
        given(tripPlaceRepository.findById(tripPlaceIdx2)).willReturn(Optional.of(tripPlace2));

        // when
        tripPlaceService.updatePlaceOrder(tripIdx, updateOrderReqDto);

        // then
        then(tripService).should().findByTripIdx(tripIdx);
        then(tripPlaceService).should().validateTeamMember(any(), eq(1L));
        then(tripPlaceService).should(times(orders.size())).validateTripDate(trip, tripDate);
        then(memberService).should().findByMemberIdx(1L);
        then(tripPlaceRepository).should().countByTripId(tripIdx);
        then(tripPlaceRepository).should().findById(tripPlaceIdx1);
        then(tripPlaceRepository).should().findById(tripPlaceIdx2);

        assertEquals(1, tripPlace1.getPlaceOrder());
        assertEquals(2, tripPlace2.getPlaceOrder());
    }

    @Test
    void updatePlaceOrder_invalidateLength() {
        // given
        Long tripIdx = 1L;
        Long tripPlaceIdx1 = 1L;
        Long tripPlaceIdx2 = 2L;
        Long tripPlaceIdx3 = 3L;
        int tripDate = 1;

        TripPlaceOrderReqDto orderDto1 = TripPlaceOrderReqDto.builder().tripPlaceIdx(tripPlaceIdx1).placeOrder(1).tripDate(1).build();
        TripPlaceOrderReqDto orderDto2 = TripPlaceOrderReqDto.builder().tripPlaceIdx(tripPlaceIdx2).placeOrder(2).tripDate(1).build();
        TripPlaceOrderReqDto orderDto3 = TripPlaceOrderReqDto.builder().tripPlaceIdx(tripPlaceIdx3).placeOrder(3).tripDate(1).build();
        UpdateOrderReqDto updateOrderReqDto = UpdateOrderReqDto.builder()
                .memberIdx(1L)
                .orders(List.of(orderDto1, orderDto2, orderDto3)) // 잘못된 길이
                .build();

        given(tripService.findByTripIdx(tripIdx)).willReturn(trip);
        willDoNothing().given(tripPlaceService).validateTeamMember(any(), anyLong());
        willDoNothing().given(tripPlaceService).validateTripDate(any(), anyInt());
        given(memberService.findByMemberIdx(1L)).willReturn(member1);
        given(tripPlaceRepository.countByTripId(tripIdx)).willReturn(2);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlaceOrder(tripIdx, updateOrderReqDto));

        // then
        assertEquals(ExceptionEnum.INVALID_ORDER_LIST.getMessage(), exception.getError().getMessage());
    }

    @Test
    void updatePlaceOrder_duplicatedList() {
        // given
        Long tripIdx = 1L;
        Long tripPlaceIdx1 = 1L;
        int tripDate = 1;

        TripPlaceOrderReqDto orderDto1 = TripPlaceOrderReqDto.builder().tripPlaceIdx(tripPlaceIdx1).placeOrder(1).tripDate(1).build();
        UpdateOrderReqDto updateOrderReqDto = UpdateOrderReqDto.builder()
                .memberIdx(1L)
                .orders(List.of(orderDto1, orderDto1)) // 중복된 트립 플레이스
                .build();

        given(tripService.findByTripIdx(tripIdx)).willReturn(trip);
        willDoNothing().given(tripPlaceService).validateTeamMember(any(), anyLong());
        willDoNothing().given(tripPlaceService).validateTripDate(any(), anyInt());
        given(memberService.findByMemberIdx(1L)).willReturn(member1);
        given(tripPlaceRepository.countByTripId(tripIdx)).willReturn(2);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlaceOrder(tripIdx, updateOrderReqDto));

        // then
        assertEquals(ExceptionEnum.INVALID_ORDER_LIST.getMessage(), exception.getError().getMessage());
    }

    @Test
    void updatePlaceOrder_teamNotMatch() {
        // given
        Long tripIdx = 1L;
        Long tripPlaceIdx1 = 1L;
        Long tripPlaceIdx2 = 2L;
        int tripDate = 1;

        TripPlaceOrderReqDto orderDto1 = TripPlaceOrderReqDto.builder().tripPlaceIdx(tripPlaceIdx1).placeOrder(1).tripDate(1).build();
        TripPlaceOrderReqDto orderDto2 = TripPlaceOrderReqDto.builder().tripPlaceIdx(tripPlaceIdx2).placeOrder(2).tripDate(1).build();
        UpdateOrderReqDto updateOrderReqDto = UpdateOrderReqDto.builder()
                .memberIdx(1L)
                .orders(List.of(orderDto1, orderDto2))
                .build();

        Trip trip = mock(Trip.class);
        TripPlace tripPlace1 = mock(TripPlace.class);
        TripPlace tripPlace2 = mock(TripPlace.class);

        given(tripPlace1.getTrip()).willReturn(trip);
        given(tripPlace2.getTrip()).willReturn(trip);
        given(trip.getTripIdx()).willReturn(999L);

        given(tripService.findByTripIdx(tripIdx)).willReturn(trip);
        willDoNothing().given(tripPlaceService).validateTeamMember(any(), anyLong());
        willDoNothing().given(tripPlaceService).validateTripDate(any(), anyInt());
        given(memberService.findByMemberIdx(1L)).willReturn(member1);
        given(tripPlaceRepository.countByTripId(tripIdx)).willReturn(2);
        given(tripPlaceRepository.findById(tripPlaceIdx1)).willReturn(Optional.of(tripPlace1));
        given(tripPlaceRepository.findById(tripPlaceIdx2)).willReturn(Optional.of(tripPlace2));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlaceOrder(tripIdx, updateOrderReqDto));

        // then
        assertEquals(ExceptionEnum.TEAM_NOT_MATCH.getMessage(), exception.getError().getMessage());
    }


    @Test
    void deleteTripPlace_success() {
        // given
        Long tripPlaceIdx = 1L;
        TripPlace tripPlaceToDelete = mock(TripPlace.class);

        given(tripPlaceRepository.findById(tripPlaceIdx)).willReturn(Optional.of(tripPlaceToDelete));
        given(tripPlaceToDelete.getPlaceOrder()).willReturn(1);
        given(tripPlaceToDelete.getTrip()).willReturn(trip);

        // when
        tripPlaceService.deleteTripPlace(tripPlaceIdx);

        // then
        then(tripPlaceRepository).should().deleteById(tripPlaceIdx);
        then(tripPlaceRepository).should().decrementPlaceOrderAfter(eq(1L), eq(1));

    }

    @Test
    void getTripPlace_success() {
        // given
        Long tripIdx = 1L;
        TripPlace tripPlace1 = createMockTripPlace(1L);
        TripPlace tripPlace2 = createMockTripPlace(2L);
        given(tripPlaceRepository.findAllByTrip_TripIdxOrderByTripDateAscPlaceOrderAsc(tripIdx)).willReturn(List.of(tripPlace1, tripPlace2));

        // when
        List<TripPlaceResDto> tripPlaces = tripPlaceService.getPlace(tripIdx);

        //then
        assertEquals(2, tripPlaces.size());
        assertEquals(tripPlace1.getTripPlaceIdx(), tripPlaces.get(0).getTripPlaceIdx());
        assertEquals(tripPlace2.getTripPlaceIdx(), tripPlaces.get(1).getTripPlaceIdx());

    }

}