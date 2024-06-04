package com.hanaro.triptogether.tripPlace.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class TripPlaceUpdateReqDto {
    private Long memberIdx;
    private List<TripPlaceUpdateAddReqDto> newPlaces; // 추가된 장소
    private List<TripPlaceOrderReqDto> orders; //전체 순서
}
