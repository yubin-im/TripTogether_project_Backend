package com.hanaro.triptogether.dues.domain.entity;

import com.hanaro.triptogether.dues.dto.request.DuesRuleRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "dues")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dues extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long duesIdx;

    private Long teamIdx;

    private int duesDate;

    private BigDecimal duesAmount;


    public void modifyDuesRule(DuesRuleRequestDto dto){
        this.duesDate=dto.getDuesDate();
        this.duesAmount=dto.getDuesAmount();
    }

}
