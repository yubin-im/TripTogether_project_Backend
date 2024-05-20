package com.hanaro.triptogether.trip;

import com.hanaro.triptogether.country.Country;
import com.hanaro.triptogether.team.domain.Team;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "trip")
@Getter
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripIdx;

    @ManyToOne
    @JoinColumn(name = "team_idx", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "country_idx", nullable = false)
    private Country country;

    @Column(nullable = false, length = 30)
    private String tripName;

    private String tripContent;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal tripGoalAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer tripDay = 1;

    private Date tripStartDay;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;
}
