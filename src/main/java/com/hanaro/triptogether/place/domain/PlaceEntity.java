package com.hanaro.triptogether.place.domain;

import java.time.LocalDateTime;

import com.hanaro.triptogether.city.domain.CityEntity;
import com.hanaro.triptogether.member.domain.Member;
import com.hanaro.triptogether.place.dto.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private CityEntity city;

    @Column(nullable = false, length = 100)
    private String placeNameKo;

    private String placeNameEng;
    private String placeImg;
    private Long categoryIdx;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", insertable=false, updatable=false)
    private Member createdBy;

    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "last_modified_by", insertable=false, updatable=false)
    private Member lastModifiedBy;

    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "deleted_by", insertable=false, updatable=false)
    private Member deletedBy;

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
