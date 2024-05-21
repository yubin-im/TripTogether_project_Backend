package com.hanaro.triptogether.tripReply.domain;

import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_reply")
@Getter
public class TripReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripReplyIdx;

    @ManyToOne
    @JoinColumn(name = "trip_place_idx", nullable = false)
    private TripPlace tripPlace;

    @ManyToOne
    @JoinColumn(name = "team_member_idx", nullable = false)
    private TeamMember teamMember;

    @Column(nullable = false, length = 255)
    private String tripReplyContent;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
