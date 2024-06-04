package com.hanaro.triptogether.tripReply.dto.response;

import com.hanaro.triptogether.tripReply.domain.TripReply;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TripReplyResDto {
    private Long tripReplyIdx;
    private Long memberIdx;
    private String teamMemberNickname;
    private String tripReplyContent;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    @Builder
    public TripReplyResDto(TripReply reply, String teamMemberNickname) {
        this.teamMemberNickname = teamMemberNickname; //탈퇴한 멤버일 경우, 대체하기 위해
        this.tripReplyIdx = reply.getTripReplyIdx();
        this.memberIdx=reply.getTeamMember().getMember().getMemberIdx();
        this.tripReplyContent=reply.getTripReplyContent();
        this.createdAt=reply.getCreatedAt();
        this.lastModifiedAt=reply.getLastModifiedAt();
    }
}