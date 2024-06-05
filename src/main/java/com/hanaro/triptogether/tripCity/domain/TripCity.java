package com.hanaro.triptogether.tripCity.domain;

import com.hanaro.triptogether.city.domain.CityEntity;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.trip.domain.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "trip_city")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripCityIdx;

    @ManyToOne
    @JoinColumn(name = "trip_idx", nullable = false)
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "city_idx")
    private CityEntity city;

    @ManyToOne
    @JoinColumn(name = "created_by", updatable=false)
    private Member createdBy;

    @Column(nullable = false)
    @CreationTimestamp
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
