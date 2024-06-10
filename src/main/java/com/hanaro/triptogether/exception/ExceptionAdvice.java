package com.hanaro.triptogether.exception;

import com.hanaro.triptogether.common.response.BaseResponse;
import com.hanaro.triptogether.common.response.ResponseStatus;
import com.hanaro.triptogether.exchangeRate.exception.AlarmNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ExceptionResponse> exceptionHandler(final ApiException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(e.getError().getStatus().value())
                .body(ExceptionResponse.builder()
                        .status(e.getError().getStatus().value())
                        .code(e.getError().getCode())
                        .message(e.getError().getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleAllExceptions(Exception ex) {
        BaseResponse response = BaseResponse.res(ResponseStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse> handleBadRequestException(BadRequestException ex) {
        BaseResponse response = BaseResponse.res(ResponseStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlarmNotFoundException.class)
    public ResponseEntity<BaseResponse> handleAlarmNotFoundException() {
        BaseResponse response = BaseResponse.res(ResponseStatus.ALARM_NOT_FOUND,ResponseStatus.ALARM_NOT_FOUND.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}