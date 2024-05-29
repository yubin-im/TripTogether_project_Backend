package com.hanaro.triptogether.tripCity.domain;

import com.hanaro.triptogether.city.domain.CityEntity;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.trip.domain.Trip;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_city")
public class TripCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripCityIdx;

    @ManyToOne
    @JoinColumn(name = "trip_idx", nullable = false)
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "city_idx", nullable = false)
    private CityEntity city;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable=false)
    private Member createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    private Member lastModifiedBy;
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private Member deletedBy;
}
