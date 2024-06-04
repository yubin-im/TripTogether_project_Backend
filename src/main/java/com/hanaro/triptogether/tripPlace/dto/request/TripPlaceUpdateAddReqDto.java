package com.hanaro.triptogether.tripPlace.dto.request;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TripPlaceUpdateAddReqDto {
    private Integer tripDate;
    private Integer placeOrder;
    private Long placeIdx;
    private BigDecimal placeAmount;
    private String placeMemo;
}