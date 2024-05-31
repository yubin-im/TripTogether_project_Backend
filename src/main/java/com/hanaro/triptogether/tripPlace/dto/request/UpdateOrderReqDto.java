package com.hanaro.triptogether.tripPlace.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateOrderReqDto {
    private Long memberIdx;
    private List<TripPlaceOrderReqDto> orders;
}
