package com.hanaro.triptogether.tripPlace.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TripPlaceOrderReqDto {
    private Long tripPlaceIdx;
    private Integer placeOrder;
    private Integer tripDate;
}
