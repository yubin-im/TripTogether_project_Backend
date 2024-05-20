package com.hanaro.triptogether.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"), //예시
    TRIP_NOT_FOUND(HttpStatus.BAD_REQUEST, "TRIP_NOT_FOUND-03","해당하는 데이터가 없습니다."), //예시

    //----------trip place
    TRIP_PLACE_NOT_FOUND(HttpStatus.BAD_REQUEST, "TRIP_PLACE_NOT_FOUND","해당하는 장소가 없습니다.");

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
