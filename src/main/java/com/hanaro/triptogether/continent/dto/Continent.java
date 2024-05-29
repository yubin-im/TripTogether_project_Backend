package com.hanaro.triptogether.continent.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Continent {
    Long continentIdx;
    String continentNameKo;
    String continentNameEng;
}
