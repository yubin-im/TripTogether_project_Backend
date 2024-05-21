package com.hanaro.triptogether.tripReply.dto.response;

import com.hanaro.triptogether.tripReply.domain.TripReply;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TripReplyResDto {
    private Long trip_reply_idx;
    private Long team_member_idx;
    private String team_member_nickname;
    private String trip_reply_content;
    private LocalDateTime created_at;
    private LocalDateTime last_modified_at;

    @Builder
    public TripReplyResDto(TripReply reply, String team_member_nickname) {
        this.team_member_nickname = team_member_nickname; //탈퇴한 멤버일 경우, 대체하기 위해
        this.trip_reply_idx = reply.getTripReplyIdx();
        this.team_member_idx=reply.getTeamMember().getTeamMemberIdx();
        this.trip_reply_content=reply.getTripReplyContent();
        this.created_at=reply.getCreatedAt();
        this.last_modified_at=reply.getLastModifiedAt();
    }
}