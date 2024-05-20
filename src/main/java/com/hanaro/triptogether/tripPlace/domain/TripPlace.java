package com.hanaro.triptogether.tripPlace.domain;

import com.hanaro.triptogether.member.Member;
import com.hanaro.triptogether.place.Place;
import com.hanaro.triptogether.trip.Trip;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_place")
@Getter
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
    @JoinColumn(name = "created_by", insertable=false, updatable=false)
    private Member createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;

}
