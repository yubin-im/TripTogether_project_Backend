package com.hanaro.triptogether.tripPlace.dto.response;

import com.hanaro.triptogether.place.dto.Place;
import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TripPlaceResDto {
    private Long tripPlaceIdx;
    private Integer tripDate;
    private Integer placeOrder;
    private Place place;
    private BigDecimal placeAmount;
    private String placeMemo;
    private int replyCount;

    @Builder
    public TripPlaceResDto(TripPlace tripPlace) {
        this.tripPlaceIdx = tripPlace.getTripPlaceIdx();
        this.tripDate = tripPlace.getTripDate();
        this.placeOrder = tripPlace.getPlaceOrder();
        this.place = tripPlace.getPlace().toPlace();
        this.placeAmount = tripPlace.getPlaceAmount();
        this.placeMemo = tripPlace.getPlaceMemo();
        this.replyCount = tripPlace.getTripReplies().size();
    }
}
