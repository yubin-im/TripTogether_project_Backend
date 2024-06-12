package com.hanaro.triptogether.tripReply.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TripReplyDeleteReqDto {
    private Long memberIdx;
    private Long tripReplyIdx;
}
