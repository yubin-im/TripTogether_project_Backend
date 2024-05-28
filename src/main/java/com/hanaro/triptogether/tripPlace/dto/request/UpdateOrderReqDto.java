package com.hanaro.triptogether.tripPlace.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateOrderReqDto {
    private String member_id;
    private Integer trip_date;
    private List<TripPlaceOrderReqDto> orders;
}
