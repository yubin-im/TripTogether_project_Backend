package com.hanaro.triptogether.tripPlace.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateOrderReqDto {
    private String member_id;
    private Integer trip_date;
    private List<TripPlaceOrderReqDto> orders;
}
