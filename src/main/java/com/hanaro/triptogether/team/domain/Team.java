package com.hanaro.triptogether.team.domain;

import com.hanaro.triptogether.account.domain.Account;
import com.hanaro.triptogether.enumeration.PreferenceType;
import com.hanaro.triptogether.enumeration.TeamType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "Team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamIdx;

    @ManyToOne
    @JoinColumn(name = "acc_idx", nullable = false)
    private Account account;

    @Column(nullable = false, length = 30)
    private String teamName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamType teamType;

    @Enumerated(EnumType.STRING)
    private PreferenceType preferenceType;

    private String teamNotice;
    private Long preferTrip;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long createdBy;

    private LocalDateTime lastModifiedAt;
    private Long lastModifiedBy;
    private LocalDateTime deletedAt;
    private Long deletedBy;
}
