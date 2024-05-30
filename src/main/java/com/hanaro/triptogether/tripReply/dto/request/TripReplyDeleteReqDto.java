package com.hanaro.triptogether.tripReply.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TripReplyDeleteReqDto {
    private Long teamMemberIdx;
    private Long tripReplyIdx;
}
