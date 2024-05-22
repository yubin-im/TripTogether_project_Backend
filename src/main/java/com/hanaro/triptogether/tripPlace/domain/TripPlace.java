package com.hanaro.triptogether.tripPlace.domain;

import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.place.domain.Place;
import com.hanaro.triptogether.trip.domain.Trip;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_place")
@Getter
@NoArgsConstructor
public class TripPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripPlaceIdx;

    @ManyToOne
    @JoinColumn(name = "trip_idx", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private Integer tripDate;

    @Column(nullable = false)
    private Integer placeOrder;

    @ManyToOne
    @JoinColumn(name = "place_idx")
    private Place place;

    private BigDecimal placeAmount;
    private String placeMemo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable=false)
    private Member createdBy;

    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    private Member lastModifiedBy;

    @Builder
    public TripPlace( Trip trip, Integer tripDate, Integer placeOrder, Place place, BigDecimal placeAmount, String placeMemo, Member member) {
        this.trip = trip;
        this.tripDate = tripDate;
        this.placeOrder = placeOrder;
        this.place = place;
        this.placeAmount = placeAmount;
        this.placeMemo = placeMemo;
        this.createdAt = LocalDateTime.now();
        this.createdBy = member;
    }

    public void update(Place place, BigDecimal placeAmount, String placeMemo, Member member){
        this.place = place;
        this.placeAmount = placeAmount;
        this.placeMemo = placeMemo;
        this.lastModifiedAt = LocalDateTime.now();
        this.lastModifiedBy = member;
    }

}
