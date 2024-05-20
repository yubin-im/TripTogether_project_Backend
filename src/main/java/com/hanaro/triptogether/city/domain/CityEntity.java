package com.hanaro.triptogether.city.domain;

import com.hanaro.triptogether.city.dto.City;
import com.hanaro.triptogether.country.Country;
import com.hanaro.triptogether.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "City")
public class CityEntity {
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

    public City toCity() {
        return City.builder()
                .cityIdx(this.cityIdx)
                .countryIdx(this.country.getCountryIdx())
                .cityNameKo(this.cityNameKo)
                .cityNameEng(this.cityNameEng).build();
    }
}
