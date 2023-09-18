package com.ljc.common;

public class CommonResp<T> {
    private String code;

    private String msg;

    private T data;

    public static <T> CommonResp<T> success(T data) {
        CommonResp<T> commonResp = new CommonResp<>();
        commonResp.setCode("success");
        commonResp.setData(data);
        return commonResp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
