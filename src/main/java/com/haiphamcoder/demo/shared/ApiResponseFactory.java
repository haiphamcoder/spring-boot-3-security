package com.haiphamcoder.demo.shared;

import org.springframework.http.HttpStatus;

public class ApiResponseFactory {
    public static ApiResponse<? extends Object> createSuccessResponse(Object data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "Success", data, null, null);
    }
}
