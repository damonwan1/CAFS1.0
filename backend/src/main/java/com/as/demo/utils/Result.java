package com.as.demo.utils;

import lombok.Data;

@Data
public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> apiResult = new Result<>();
        apiResult.setMsg("操作成功");
        apiResult.setData(data);
        apiResult.setCode("10000");
        return apiResult;
    }

    public static <T> Result<T> failure(T data) {
        Result<T> apiResult = new Result<>();
        apiResult.setMsg("操作失败");
        apiResult.setData(data);
        apiResult.setCode("20000");
        return apiResult;
    }
}
