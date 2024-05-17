package com.hanaro.triptogether.city;

import com.hanaro.triptogether.country.Country;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "City")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityIdx;

    @ManyToOne
    @JoinColumn(name = "country_idx", nullable = false)
    private Country country;

    @Column(nullable = false, length = 100)
    private String cityNameKo;

    private String cityNameEng;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;
}
