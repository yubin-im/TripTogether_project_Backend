package com.hanaro.triptogether.tripReply.dto.request;

import com.hanaro.triptogether.teamMember.domain.TeamMember;
import com.hanaro.triptogether.tripPlace.domain.TripPlace;
import com.hanaro.triptogether.tripReply.domain.TripReply;
import lombok.Getter;


@Getter
public class TripReplyReqDto {
    private Long team_member_idx;
    private String trip_reply_content;

    public TripReply toEntity(TripPlace tripPlace, TeamMember teamMember, String trip_reply_content) {
        return TripReply.builder()
                .tripPlace(tripPlace)
                .teamMember(teamMember)
                .tripReplyContent(trip_reply_content)
                .build();
    }
}