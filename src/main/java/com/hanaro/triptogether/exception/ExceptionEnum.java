package com.hanaro.triptogether.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {

    //------member
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER_NOT_FOUND","해당하는 멤버가 없습니다."),

    //---------team member
    TEAM_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "TEAM_MEMBER_NOT_FOUND","해당하는 팀원IDX가 없습니다."),
    INVALID_TEAM_MEMBER(HttpStatus.FORBIDDEN, "INVALID_TEAM_MEMBER","팀원이 아닙니다."),
    INVALID_TEAM_MEMBER_ROLE(HttpStatus.FORBIDDEN, "INVALID_TEAM_MEMBER_ROLE","접근 권한이 없습니다."),

    //-----place
    PLACE_NOT_FOUND(HttpStatus.BAD_REQUEST, "PLACE_NOT_FOUND","해당하는 명소가 없습니다."),

    //---------trip
    TRIP_NOT_FOUND(HttpStatus.BAD_REQUEST, "TRIP_NOT_FOUND","해당하는 여행이 없습니다."),

    //----------trip place
    TRIP_PLACE_NOT_FOUND(HttpStatus.BAD_REQUEST, "TRIP_PLACE_NOT_FOUND","해당하는 여행 장소가 없습니다."),
    INVALID_ORDER_LIST(HttpStatus.BAD_REQUEST, "INVALID_ORDER_LIST","잘못된 일정 목록입니다."),
    INVALID_TRIP_DATE(HttpStatus.BAD_REQUEST, "INVALID_TRIP_DATE","잘못된 여행 일정입니다."),
    TEAM_NOT_MATCH(HttpStatus.FORBIDDEN, "TEAM_NOT_MATCH","접근 권한이 없습니다."),

    //----------trip place reply
    TRIP_REPLY_NOT_FOUND(HttpStatus.BAD_REQUEST, "TRIP_REPLY_NOT_FOUND","해당하는 댓글이 없습니다."),
    TRIP_REPLY_MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "TRIP_REPLY_MEMBER_NOT_MATCH","댓글 수정 및 삭제는 본인만 가능합니다"),
    TRIP_INFO_NOT_MATCH(HttpStatus.FORBIDDEN, "TRIP_INFO_NOT_MATCH","팀 정보가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
