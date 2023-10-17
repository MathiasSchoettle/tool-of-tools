package de.mscho.toftws.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiResponse(
        String status,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Object data) {

    public static ApiResponse success(Object data) {
        return new ApiResponse("success", null, data);
    }
    public static ApiResponse success() {
        return new ApiResponse("success", null, null);
    }
    public static ApiResponse fail(Object data) {
        return new ApiResponse("fail", null, data);
    }
    public static ApiResponse error(String message, Object data) {
        return new ApiResponse("error", message, data);
    }
}
