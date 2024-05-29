package com.hanaro.triptogether.tripReply.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TripReplyDeleteReqDto {
    private Long team_member_idx;
    private Long trip_reply_idx;
}
