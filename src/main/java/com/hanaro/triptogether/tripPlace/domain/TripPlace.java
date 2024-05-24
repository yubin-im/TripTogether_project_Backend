package com.hanaro.triptogether.tripPlace.domain;

import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.place.domain.PlaceEntity;
import com.hanaro.triptogether.trip.domain.Trip;
import com.hanaro.triptogether.tripReply.domain.TripReply;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private PlaceEntity place;

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

    @OneToMany(mappedBy = "tripPlace", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TripReply> tripReplies;

    @Builder
    public TripPlace( Trip trip, Integer tripDate, Integer placeOrder, PlaceEntity place, BigDecimal placeAmount, String placeMemo, Member member) {
        this.trip = trip;
        this.tripDate = tripDate;
        this.placeOrder = placeOrder;
        this.place = place;
        this.placeAmount = placeAmount;
        this.placeMemo = placeMemo;
        this.createdAt = LocalDateTime.now();
        this.createdBy = member;
    }

    public void update(PlaceEntity place, BigDecimal placeAmount, String placeMemo, Member member){
        this.place = place;
        this.placeAmount = placeAmount;
        this.placeMemo = placeMemo;
        this.lastModifiedAt = LocalDateTime.now();
        this.lastModifiedBy = member;
    }
    public void updateOrder(Integer placeOrder, Member member){
        this.placeOrder=placeOrder;
        this.lastModifiedAt = LocalDateTime.now();
        this.lastModifiedBy = member;
    }

}
