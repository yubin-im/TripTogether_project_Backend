package com.hanaro.triptogether.country.domain;

import com.hanaro.triptogether.city.domain.CityEntity;
import com.hanaro.triptogether.continent.domain.ContinentEntity;
import com.hanaro.triptogether.country.dto.Country;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "country")
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countryIdx;

    @ManyToOne
    @JoinColumn(name = "continent_idx", nullable = false)
    private ContinentEntity continent;

    @Column(nullable = false, length = 100)
    private String countryNameKo;

    private String countryNameEng;

    @Column(name = "naver_id")
    private String naverId;

    @Column(name = "country_img")
    private String countryImg;

    @OneToMany(mappedBy = "country")
    private List<CityEntity> cities;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;

    public Country toCountry() {
        return Country.builder()
                .countryIdx(this.countryIdx)
                .countryNameKo(this.countryNameKo)
                .countryNameEng(this.countryNameEng)
                .naverId(this.naverId)
                .countryImg(this.countryImg)
                .build();
    }
}
