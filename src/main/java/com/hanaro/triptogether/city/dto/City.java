package com.hanaro.triptogether.city.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class City {
    Long cityIdx;
    Long countryIdx;
    String cityNameKo;
    String cityNameEng;
}
