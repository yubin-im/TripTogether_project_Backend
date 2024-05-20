package com.hanaro.triptogether.exchangeRate.dto.request;

import com.hanaro.triptogether.dues.Dues;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuesRuleRequestDto {

    private int gatheringIdx;
    private LocalDateTime duesDate;
    private Double duesAmount;

    public Dues toEntity() {
        return Dues.builder()
                .duesAmount(duesAmount)
                .duesDate(duesDate)
                .ga
    }
}
