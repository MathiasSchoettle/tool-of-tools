package de.mscho.toftws.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiResponse(
        ApiResponseStatus status,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object data) {

    public static ApiResponse success(Object data) {
        return new ApiResponse(ApiResponseStatus.SUCCESS, null, data);
    }
    public static ApiResponse success() {
        return new ApiResponse(ApiResponseStatus.SUCCESS, null, null);
    }
    public static ApiResponse fail(Object data) {
        return new ApiResponse(ApiResponseStatus.FAILURE, null, data);
    }
    public static ApiResponse error(String message) {
        return new ApiResponse(ApiResponseStatus.ERROR, message, null);
    }
    public static ApiResponse error(String message, Object data) {
        return new ApiResponse(ApiResponseStatus.ERROR, message, data);
    }

    private enum ApiResponseStatus {
        SUCCESS, FAILURE, ERROR
    }
}
