package com.hanaro.triptogether.tripPlace;

import com.hanaro.triptogether.place.Place;
import com.hanaro.triptogether.trip.Trip;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Trip_place")
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

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;
}
