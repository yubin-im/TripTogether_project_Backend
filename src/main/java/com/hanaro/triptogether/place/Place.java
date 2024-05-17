package com.hanaro.triptogether.place;

import java.time.LocalDateTime;

import com.hanaro.triptogether.city.City;
import jakarta.persistence.*;
@Entity
@Table(name = "Place")
public class Place {
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
}
