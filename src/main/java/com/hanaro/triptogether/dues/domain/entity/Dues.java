package com.hanaro.triptogether.dues.domain.entity;

import com.hanaro.triptogether.dues.domain.entity.BaseEntity;
import com.hanaro.triptogether.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Dues")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dues extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long duesIdx;

    private Long teamIdx;

    private LocalDate duesDate;

    private BigDecimal duesAmount;



}
