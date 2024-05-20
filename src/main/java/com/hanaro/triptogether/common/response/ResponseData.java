package com.hanaro.triptogether.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor // 생성자를 자동으로 만들어 준다
@Builder
public class ResponseData<T> {
    private int statusCode;
    private boolean success;
    private String responseMessage;
    private T data;

    public ResponseData(final int statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public static<T> ResponseData<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    public static<T> ResponseData<T> res(final int statusCode, final String responseMessage, final T t) {
        return ResponseData.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }
}
