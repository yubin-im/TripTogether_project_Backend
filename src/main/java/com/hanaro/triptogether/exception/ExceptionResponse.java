package com.hanaro.triptogether.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponse {
    private int status;
    private String code;
    private String message;

    @Builder
    public ExceptionResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}