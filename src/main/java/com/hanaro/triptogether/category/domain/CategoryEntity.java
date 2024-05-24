package com.hanaro.triptogether.category.domain;

import com.hanaro.triptogether.category.dto.Category;
import com.hanaro.triptogether.member.domain.Member;
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

    public Category toCategory() {
        return Category.builder()
                .categoryIdx(this.categoryIdx)
                .categoryName(this.categoryName)
                .build();
    }
}
