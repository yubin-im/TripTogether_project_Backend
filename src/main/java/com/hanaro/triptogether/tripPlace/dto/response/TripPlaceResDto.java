package com.hanaro.triptogether.tripPlace.dto.response;

import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TripPlaceResDto {
    private Long trip_place_idx;
    private Integer trip_date;
    private Integer place_order;
    private Long place_idx;
    private BigDecimal place_amount;
    private String place_memo;

    @Builder
    public TripPlaceResDto(TripPlace tripPlace) {
        this.trip_place_idx = tripPlace.getTripPlaceIdx();
        this.trip_date = tripPlace.getTripDate();
        this.place_order = tripPlace.getPlaceOrder();
        this.place_idx = tripPlace.getPlace().toPlace().getPlaceIdx();
        this.place_amount = tripPlace.getPlaceAmount();
        this.place_memo = tripPlace.getPlaceMemo();
    }
}
