package com.hanaro.triptogether.tripPlace.dto.response;

import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TripPlaceResDto {
    private Long tripPlaceIdx;
    private Integer tripDate;
    private Integer placeOrder;
    private Long placeIdx;
    private BigDecimal placeAmount;
    private String placeMemo;

    @Builder
    public TripPlaceResDto(TripPlace tripPlace) {
        this.tripPlaceIdx = tripPlace.getTripPlaceIdx();
        this.tripDate = tripPlace.getTripDate();
        this.placeOrder = tripPlace.getPlaceOrder();
        this.placeIdx = tripPlace.getPlace().toPlace().getPlaceIdx();
        this.placeAmount = tripPlace.getPlaceAmount();
        this.placeMemo = tripPlace.getPlaceMemo();
    }
}
