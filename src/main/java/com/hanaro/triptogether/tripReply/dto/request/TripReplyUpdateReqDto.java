package com.hanaro.triptogether.tripReply.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TripReplyUpdateReqDto {
    private Long teamMemberIdx;
    private Long tripReplyIdx;
    private String tripReplyContent;
}
