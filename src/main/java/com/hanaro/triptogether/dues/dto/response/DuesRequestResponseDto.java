package com.hanaro.triptogether.dues.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DuesRequestResponseDto {

    private String teamName;
    private List<String> memberNames;
    private BigDecimal duesAmount;
}
