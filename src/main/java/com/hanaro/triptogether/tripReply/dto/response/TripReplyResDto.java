package com.hanaro.triptogether.tripReply.dto.response;

import com.hanaro.triptogether.tripReply.domain.TripReply;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TripReplyResDto {
    private Long trip_reply_idx;
    private Long team_member_idx;
    private String trip_reply_content;
    private LocalDateTime created_at;
    private LocalDateTime last_modified_at;

    @Builder
    public TripReplyResDto(TripReply reply){
        this.trip_reply_idx = reply.getTripReplyIdx();
        this.team_member_idx=reply.getTeamMember().getTeamMemberIdx();
        this.trip_reply_content=reply.getTripReplyContent();
        this.created_at=reply.getCreatedAt();
        this.last_modified_at=reply.getLastModifiedAt();
    }
}