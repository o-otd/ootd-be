package com.ootd.be.api;

import com.ootd.be.util.StringUtil;

import lombok.Data;

@Data
public class ApiResponse<T> {

    public boolean ok;

    private T data;

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

    public static ApiResponse<Object> fail(String message) {
        ApiResponse vo = new ApiResponse();
        vo.ok = false;
        vo.error = message;
        return vo;
    }

    public static ApiResponse<Object> fail(String format, Object...args) {
        ApiResponse vo = new ApiResponse();
        vo.ok = false;
        vo.error = StringUtil.format(format, args);
        return vo;
    }

}
