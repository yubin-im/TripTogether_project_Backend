package com.hanaro.triptogether.tripReply;

import com.hanaro.triptogether.teamMember.TeamMember;
import com.hanaro.triptogether.tripPlace.TripPlace;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Trip_reply")
public class TripReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripReplyIdx;

    @ManyToOne
    @JoinColumn(name = "trip_place_idx", nullable = false)
    private TripPlace tripPlace;

    @ManyToOne
    @JoinColumn(name = "gathering_member_idx", nullable = false)
    private TeamMember teamMember;

    @Column(nullable = false, length = 255)
    private String tripReplyContent;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;
    private LocalDateTime deletedAt;
}
