package com.hanaro.triptogether.trip.domain;

import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.team.domain.Team;
import com.hanaro.triptogether.trip.dto.response.TripResDto;
import com.hanaro.triptogether.tripCity.domain.TripCity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trip")
@Getter
@Setter
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

    private Integer tripImg;

    private LocalDate tripStartDay;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripCity> tripCities;

    @Column(nullable = false)
    @CreationTimestamp
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

    public TripResDto toTrip() {
        return TripResDto.builder()
                .teamIdx(this.getTeam().getTeamIdx())
                .teamName(this.getTeam().getTeamName())
                .tripIdx(this.getTripIdx())
                .tripDay(this.getTripDay())
                .tripContent(this.getTripContent())
                .tripGoalAmount(this.getTripGoalAmount())
                .tripName(this.getTripName())
                .tripStartDay(this.getTripStartDay())
                .build();
    }

    public void update(String tripName,
                       String tripContent,
                       BigDecimal tripGoalAmount,
                       Integer tripDay,
                       Integer tripImg,
                       LocalDate tripStartDay,
                       List<TripCity> tripCities,
                       Member member) {
        this.tripName = tripName;
        this.tripContent = tripContent;
        this.tripGoalAmount = tripGoalAmount;
        this.tripDay = tripDay;
        this.tripImg = tripImg;
        this.tripStartDay = tripStartDay;
        this.lastModifiedAt = LocalDateTime.now();
        this.lastModifiedBy = member;

        // 기존 TripCity 목록을 업데이트합니다.
        this.tripCities.clear();
        this.tripCities.addAll(tripCities);
    }
}
