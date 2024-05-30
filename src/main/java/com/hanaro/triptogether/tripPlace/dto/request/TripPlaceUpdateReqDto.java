package com.hanaro.triptogether.tripPlace.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TripPlaceUpdateReqDto {
    private Long placeIdx;
    private BigDecimal placeAmount;
    private String placeMemo;
    private Long memberIdx; //수정자 idx
}