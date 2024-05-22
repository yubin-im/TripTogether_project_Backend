package com.hanaro.triptogether.tripPlace.dto.request;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TripPlaceAddReqDto {
    private Long trip_idx;
    private Integer trip_date;
    private Long place_idx;
    private BigDecimal place_amount;
    private String place_memo;
    private String member_id; //작성자 id
}