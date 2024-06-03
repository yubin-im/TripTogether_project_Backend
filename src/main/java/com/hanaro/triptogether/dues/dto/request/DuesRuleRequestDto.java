package com.hanaro.triptogether.dues.dto.request;

import com.hanaro.triptogether.dues.domain.entity.Dues;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class DuesRuleRequestDto {

    private Long teamIdx;
    private int duesDate;
    private BigDecimal duesAmount;

    public Dues toEntity() {
        return Dues.builder()
                .duesAmount(duesAmount)
                .duesDate(duesDate)
                .teamIdx(teamIdx)
                .build();
    }
}
