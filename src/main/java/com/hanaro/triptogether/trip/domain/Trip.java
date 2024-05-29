package com.hanaro.triptogether.trip.domain;

import com.hanaro.triptogether.country.domain.CountryEntity;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.team.domain.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "trip")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripIdx;

    @ManyToOne
    @JoinColumn(name = "team_idx", nullable = false)
    private Team team;

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

    @ManyToOne
    @JoinColumn(name = "created_by", updatable=false)
    private Member createdBy;

    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    private Member lastModifiedBy;
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private Member deletedBy;
}
