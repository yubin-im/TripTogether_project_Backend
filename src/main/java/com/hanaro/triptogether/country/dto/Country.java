package com.hanaro.triptogether.country.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Country {
    Long countryIdx;
    String countryNameKo;
    String countryNameEng;
    String naverId;
    String countryImg;
}
