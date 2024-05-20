package com.hanaro.triptogether.category.domain;

import com.hanaro.triptogether.category.dto.Category;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "Category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryIdx;

    @Column(nullable = false, length = 100)
    private String categoryName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;

    public Category toCategory() {
        return Category.builder()
                .categoryIdx(this.categoryIdx)
                .categoryName(this.categoryName)
                .build();
    }
}
