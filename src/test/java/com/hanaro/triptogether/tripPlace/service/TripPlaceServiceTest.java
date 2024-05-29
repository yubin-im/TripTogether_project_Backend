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
import com.hanaro.triptogether.tripPlace.dto.request.TripPlaceUpdateReqDto;
import com.hanaro.triptogether.tripPlace.dto.request.UpdateOrderReqDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                .tripStartDay(dateFormat.parse("2025-01-01"))
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
                .trip_idx(1L)
                .member_id("member1")
                .trip_date(1)
                .place_idx(1L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTrip_idx())).willThrow(new ApiException(ExceptionEnum.TRIP_NOT_FOUND));

        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.addPlace(tripPlaceAddReqDto));

        assertEquals( ExceptionEnum.TRIP_NOT_FOUND.getMessage(), exception.getMessage());
        then(tripPlaceRepository).should(never()).save(any(TripPlace.class));
    }

    @Test
    void addPlace_InvalidTeamMember() {
        // given
        TripPlaceAddReqDto tripPlaceAddReqDto = TripPlaceAddReqDto.builder()
                .trip_idx(1L)
                .member_id("member1")
                .trip_date(1)
                .place_idx(1L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTrip_idx())).willReturn(trip);
        willThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER))
                .given(teamMemberService).findTeamMemberByMemberId(anyString());

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
                .trip_idx(1L)
                .member_id("member1")
                .trip_date(4) // trip_date가 tripDay보다 큼
                .place_idx(1L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTrip_idx())).willReturn(trip);

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
                .trip_idx(1L)
                .member_id("member1")
                .trip_date(1)
                .place_idx(1L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTrip_idx())).willReturn(trip);
        given(placeService.findByPlaceIdx(tripPlaceAddReqDto.getPlace_idx()))
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
                .trip_idx(1L)
                .member_id("member1")
                .trip_date(1)
                .place_idx(1L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTrip_idx())).willReturn(trip);
        given(placeService.findByPlaceIdx(tripPlaceAddReqDto.getPlace_idx())).willReturn(place);
        given(memberService.findByMemberId(tripPlaceAddReqDto.getMember_id()))
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
                .trip_idx(1L)
                .member_id("member1")
                .trip_date(1)
                .place_idx(1L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .build();

        given(tripService.findByTripIdx(tripPlaceAddReqDto.getTrip_idx())).willReturn(trip);
        given(placeService.findByPlaceIdx(tripPlaceAddReqDto.getPlace_idx())).willReturn(place);
        given(memberService.findByMemberId(tripPlaceAddReqDto.getMember_id())).willReturn(member1);
        given(tripPlaceRepository.countByTripId(tripPlaceAddReqDto.getTrip_idx(), tripPlaceAddReqDto.getTrip_date()))
                .willReturn(0);

        // when
        tripPlaceService.addPlace(tripPlaceAddReqDto);

        // then
        then(tripService).should(times(1)).findByTripIdx(tripPlaceAddReqDto.getTrip_idx());
        then(placeService).should(times(1)).findByPlaceIdx(tripPlaceAddReqDto.getPlace_idx());
        then(memberService).should(times(1)).findByMemberId(tripPlaceAddReqDto.getMember_id());
        then(tripPlaceRepository).should(times(1)).save(any(TripPlace.class));

        ArgumentCaptor<TripPlace> tripPlaceCaptor = ArgumentCaptor.forClass(TripPlace.class);
        then(tripPlaceRepository).should().save(tripPlaceCaptor.capture());
        TripPlace capturedTripPlace = tripPlaceCaptor.getValue();

        assertEquals(trip, capturedTripPlace.getTrip());
        assertEquals(tripPlaceAddReqDto.getTrip_date(), capturedTripPlace.getTripDate());
        assertEquals(1, capturedTripPlace.getPlaceOrder());
        assertEquals(place, capturedTripPlace.getPlace());
        assertEquals(tripPlaceAddReqDto.getPlace_amount(), capturedTripPlace.getPlaceAmount());
        assertEquals(tripPlaceAddReqDto.getPlace_memo(), capturedTripPlace.getPlaceMemo());
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
                .build();
    }

    @Test
    void updatePlace_Success() {
        // given
        Long tripPlaceIdx = 1L; // 수정할 일정 idx
        TripPlaceUpdateReqDto dto = TripPlaceUpdateReqDto.builder()
                .place_idx(2L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .member_id("member2")
                .build();

        TripPlace tripPlace = createMockTripPlace(tripPlaceIdx); // 가짜 TripPlace 객체 생성

        given(tripPlaceRepository.findById(tripPlaceIdx)).willReturn(Optional.of(tripPlace));
        given(placeService.findByPlaceIdx(dto.getPlace_idx())).willReturn(
                PlaceEntity.builder()
                .placeIdx(2L)
                .city(city)
                .placeNameKo("placeNameKo")
                .createdAt(LocalDateTime.now())
                .createdBy(member1)
                .build());
        given(memberService.findByMemberId(dto.getMember_id())).willReturn(member2);

        // when
        tripPlaceService.updatePlace(tripPlaceIdx, dto);

        // then
        // 수정된 값을 다시 DB에서 가져와서 확인
        TripPlace updatedTripPlace = tripPlaceRepository.findById(tripPlaceIdx).orElse(null);
        assertNotNull(updatedTripPlace);
        assertEquals(dto.getPlace_idx(), updatedTripPlace.getPlace().toPlace().getPlaceIdx());
        assertEquals(dto.getPlace_amount(), updatedTripPlace.getPlaceAmount());
        assertEquals(dto.getPlace_memo(), updatedTripPlace.getPlaceMemo());
        assertEquals(dto.getMember_id(), updatedTripPlace.getLastModifiedBy().getMemberId());
    }

    @Test
    void updatePlace_tripPlaceNotFound() {
        // given
        Long trip_place_idx = 100L; // 없는 trip_place_idx
        TripPlaceUpdateReqDto tripPlaceUpdateReqDto = TripPlaceUpdateReqDto.builder()
                .place_idx(1L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .member_id("member1")
                .build();

        given(tripPlaceRepository.findById(trip_place_idx)).willThrow(new ApiException(ExceptionEnum.TRIP_PLACE_NOT_FOUND));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlace(trip_place_idx, tripPlaceUpdateReqDto));

        // then
        assertEquals(ExceptionEnum.TRIP_PLACE_NOT_FOUND.getMessage(), exception.getError().getMessage());
    }
    @Test
    void updatePlace_memberNotInTeam() {
        // given
        Long tripPlaceIdx = 1L; // 수정할 일정 idx
        TripPlaceUpdateReqDto dto = TripPlaceUpdateReqDto.builder()
                .place_idx(2L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .member_id("nonMember")
                .build();

        TripPlace tripPlace = createMockTripPlace(tripPlaceIdx); // 가짜 TripPlace 객체 생성

        given(tripPlaceRepository.findById(tripPlaceIdx)).willReturn(Optional.of(tripPlace));
        given(memberService.findByMemberId(dto.getMember_id())).willReturn(member2); // `member2`가 팀의 멤버가 아님을 가정

        doThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER)).when(tripPlaceService).validateTeamMember(any(Team.class), eq(dto.getMember_id()));

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlace(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.INVALID_TEAM_MEMBER.getMessage(), exception.getError().getMessage());
    }

    @Test
    void updatePlace_placeNotFound() {
        // given
        Long tripPlaceIdx = 1L; // 수정할 일정 idx
        TripPlaceUpdateReqDto dto = TripPlaceUpdateReqDto.builder()
                .place_idx(2L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .member_id("member2")
                .build();

        TripPlace tripPlace = createMockTripPlace(tripPlaceIdx); // 가짜 TripPlace 객체 생성

        given(tripPlaceRepository.findById(tripPlaceIdx)).willReturn(Optional.of(tripPlace));
        given(placeService.findByPlaceIdx(dto.getPlace_idx())).willThrow(new ApiException(ExceptionEnum.PLACE_NOT_FOUND));
        given(memberService.findByMemberId(dto.getMember_id())).willReturn(member2);

        // when
        ApiException exception = assertThrows(ApiException.class, () -> tripPlaceService.updatePlace(tripPlaceIdx, dto));

        // then
        assertEquals(ExceptionEnum.PLACE_NOT_FOUND.getMessage(), exception.getError().getMessage());
    }

    @Test
    void updatePlace_memberNotFound() {
        // given
        Long tripPlaceIdx = 1L; // 수정할 일정 idx
        TripPlaceUpdateReqDto dto = TripPlaceUpdateReqDto.builder()
                .place_idx(2L)
                .place_amount(BigDecimal.valueOf(100))
                .place_memo("Memo")
                .member_id("nonExistingMember")
                .build();

        TripPlace tripPlace = createMockTripPlace(tripPlaceIdx); // 가짜 TripPlace 객체 생성

        given(tripPlaceRepository.findById(tripPlaceIdx)).willReturn(Optional.of(tripPlace));
        given(placeService.findByPlaceIdx(dto.getPlace_idx())).willReturn(
                PlaceEntity.builder()
                        .placeIdx(2L)
                        .city(city)
                        .placeNameKo("placeNameKo")
                        .createdAt(LocalDateTime.now())
                        .createdBy(member1)
                        .build());
        given(memberService.findByMemberId(dto.getMember_id())).willThrow(new ApiException(ExceptionEnum.INVALID_TEAM_MEMBER));

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

        TripPlaceOrderReqDto orderDto1 = TripPlaceOrderReqDto.builder().trip_place_idx(tripPlaceIdx1).build();
        TripPlaceOrderReqDto orderDto2 = TripPlaceOrderReqDto.builder().trip_place_idx(tripPlaceIdx2).build();
        UpdateOrderReqDto updateOrderReqDto = UpdateOrderReqDto.builder()
                .trip_date(tripDate)
                .member_id("member1")
                .orders(List.of(orderDto1, orderDto2))
                .build();

        TripPlace tripPlace1 = createMockTripPlace(tripPlaceIdx1, 1);
        TripPlace tripPlace2 = createMockTripPlace(tripPlaceIdx2, 2);

        given(tripService.findByTripIdx(tripIdx)).willReturn(trip);
        willDoNothing().given(tripPlaceService).validateTeamMember(any(), anyString());
        willDoNothing().given(tripPlaceService).validateTripDate(any(), anyInt());
        given(memberService.findByMemberId("member1")).willReturn(member1);
        given(tripPlaceRepository.countByTripId(tripIdx, tripDate)).willReturn(2);
        given(tripPlaceRepository.findById(tripPlaceIdx1)).willReturn(Optional.of(tripPlace1));
        given(tripPlaceRepository.findById(tripPlaceIdx2)).willReturn(Optional.of(tripPlace2));

        // when
        tripPlaceService.updatePlaceOrder(tripIdx, updateOrderReqDto);

        // then
        then(tripService).should().findByTripIdx(tripIdx);
        then(tripPlaceService).should().validateTeamMember(any(), eq("member1"));
        then(tripPlaceService).should().validateTripDate(trip, tripDate);
        then(memberService).should().findByMemberId("member1");
        then(tripPlaceRepository).should().countByTripId(tripIdx, tripDate);
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

        TripPlaceOrderReqDto orderDto1 = TripPlaceOrderReqDto.builder().trip_place_idx(tripPlaceIdx1).build();
        TripPlaceOrderReqDto orderDto2 = TripPlaceOrderReqDto.builder().trip_place_idx(tripPlaceIdx2).build();
        TripPlaceOrderReqDto orderDto3 = TripPlaceOrderReqDto.builder().trip_place_idx(tripPlaceIdx3).build();
        UpdateOrderReqDto updateOrderReqDto = UpdateOrderReqDto.builder()
                .trip_date(tripDate)
                .member_id("member1")
                .orders(List.of(orderDto1, orderDto2, orderDto3)) // 잘못된 길이
                .build();

        given(tripService.findByTripIdx(tripIdx)).willReturn(trip);
        willDoNothing().given(tripPlaceService).validateTeamMember(any(), anyString());
        willDoNothing().given(tripPlaceService).validateTripDate(any(), anyInt());
        given(memberService.findByMemberId("member1")).willReturn(member1);
        given(tripPlaceRepository.countByTripId(tripIdx, tripDate)).willReturn(2);

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

        TripPlaceOrderReqDto orderDto1 = TripPlaceOrderReqDto.builder().trip_place_idx(tripPlaceIdx1).build();
        UpdateOrderReqDto updateOrderReqDto = UpdateOrderReqDto.builder()
                .trip_date(tripDate)
                .member_id("member1")
                .orders(List.of(orderDto1, orderDto1)) // 중복된 트립 플레이스
                .build();

        given(tripService.findByTripIdx(tripIdx)).willReturn(trip);
        willDoNothing().given(tripPlaceService).validateTeamMember(any(), anyString());
        willDoNothing().given(tripPlaceService).validateTripDate(any(), anyInt());
        given(memberService.findByMemberId("member1")).willReturn(member1);
        given(tripPlaceRepository.countByTripId(tripIdx, tripDate)).willReturn(2);

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

        TripPlaceOrderReqDto orderDto1 = TripPlaceOrderReqDto.builder().trip_place_idx(tripPlaceIdx1).build();
        TripPlaceOrderReqDto orderDto2 = TripPlaceOrderReqDto.builder().trip_place_idx(tripPlaceIdx2).build();
        UpdateOrderReqDto updateOrderReqDto = UpdateOrderReqDto.builder()
                .trip_date(tripDate)
                .member_id("member1")
                .orders(List.of(orderDto1, orderDto2))
                .build();

        Trip trip = mock(Trip.class);
        TripPlace tripPlace1 = mock(TripPlace.class);
        TripPlace tripPlace2 = mock(TripPlace.class);

        given(tripPlace1.getTrip()).willReturn(trip);
        given(tripPlace2.getTrip()).willReturn(trip);
        given(trip.getTripIdx()).willReturn(999L);

        given(tripService.findByTripIdx(tripIdx)).willReturn(trip);
        willDoNothing().given(tripPlaceService).validateTeamMember(any(), anyString());
        willDoNothing().given(tripPlaceService).validateTripDate(any(), anyInt());
        given(memberService.findByMemberId("member1")).willReturn(member1);
        given(tripPlaceRepository.countByTripId(tripIdx, tripDate)).willReturn(2);
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

}