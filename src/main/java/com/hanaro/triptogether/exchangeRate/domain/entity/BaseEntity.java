package com.hanaro.triptogether.exchangeRate.domain.entity;

import com.hanaro.triptogether.member.Member;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
public class BaseEntity {

    @CreatedDate                            // 엔티티가 생성되어 저장될 때 시간을 자동으로 저장함
    @Column(updatable = false)              // update 시점에 컬럼 관련 타입이나 이름 변경 막는다.
    private LocalDateTime createdAt;

    @LastModifiedDate                       // 엔티티의 값을 변경할 때 시간을 자동으로 저장함
    private LocalDateTime lastModifiedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    @Column(updatable = false)
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "deleted_by", insertable=false, updatable=false)
    private Member deletedBy;
}
