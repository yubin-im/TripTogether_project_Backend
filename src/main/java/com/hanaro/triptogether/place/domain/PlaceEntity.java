package com.hanaro.triptogether.place.domain;

import java.time.LocalDateTime;

import com.hanaro.triptogether.city.City;
import com.hanaro.triptogether.place.dto.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Place")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeIdx;

    @ManyToOne
    @JoinColumn(name = "city_idx", nullable = false)
    private City city;

    @Column(nullable = false, length = 100)
    private String placeNameKo;

    private String placeNameEng;
    private String placeImg;
    private Long categoryIdx;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;

    public Place toPlace() {
        return Place.builder()
                .placeIdx(this.placeIdx)
                .cityIdx(this.city.getCityIdx())
                .placeNameKo(this.placeNameKo)
                .placeNameEng(this.placeNameEng)
                .placeImg(this.placeImg)
                .categoryIdx(this.categoryIdx)
                .build();
    }
}
