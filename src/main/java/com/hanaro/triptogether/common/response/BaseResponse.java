package com.hanaro.triptogether.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static com.hanaro.triptogether.common.response.ResponseStatus.SUCCESS;

@Data
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

    private final int code;
    private final String message;
    private final T data;

    public static<T> BaseResponse<T> res(final ResponseStatus statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    public static<T> BaseResponse<T> res(final ResponseStatus statusCode, final String responseMessage, final T t) {
        return BaseResponse.<T>builder()
                .data(t)
                .code(statusCode.getHttpStatusCode())
                .message(responseMessage)
                .build();
    }

}
