package com.hanaro.triptogether.continent.domain;

import com.hanaro.triptogether.continent.dto.Continent;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;

@Table(name = "continent")
@Entity
public class ContinentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long continentIdx;

    @Column(nullable = false)
    private String continentNameKo;

    private String continentNameEng;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;


    public Continent toContinent() {
        return Continent.builder()
                .continentIdx(this.continentIdx)
                .continentNameKo(this.continentNameKo)
                .continentNameEng(this.continentNameEng)
                .build();
    }
}
