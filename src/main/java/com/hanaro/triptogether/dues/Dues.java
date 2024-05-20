package com.hanaro.triptogether.dues;

import com.hanaro.triptogether.exchangeRate.domain.entity.BaseEntity;
import com.hanaro.triptogether.team.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Dues")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dues extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long duesIdx;

    @ManyToOne
    @JoinColumn(name = "gathering_idx", nullable = false)
    private Team team;

    @Column(nullable = false)
    private Integer duesDate;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal duesAmount = BigDecimal.ZERO;

}
