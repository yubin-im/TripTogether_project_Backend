package com.hanaro.triptogether.dues;

import com.hanaro.triptogether.team.domain.Team;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Dues")
public class Dues {
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

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;
}
