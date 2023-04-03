package com.ootd.be.api;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private boolean ok;

    private T data;

    private int code;
    private String error;

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse vo = new ApiResponse();
        vo.ok = true;
        vo.data = data;
        return vo;
    }

    public static ApiResponse<Object> ok() {
        ApiResponse vo = new ApiResponse();
        vo.ok = true;
        return vo;
    }

    public static ApiResponse<String> fail(int code, String error) {
        ApiResponse vo = new ApiResponse();
        vo.ok = false;
        vo.code = code;
        vo.error = error;
        return vo;
    }

}
