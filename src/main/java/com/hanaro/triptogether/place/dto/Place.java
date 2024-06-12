package com.hanaro.triptogether.place.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Place {
    Long placeIdx;
    Long cityIdx;
    String placeNameKo;
    String placeNameEng;
    String placeImg;
    Long categoryIdx;
}
