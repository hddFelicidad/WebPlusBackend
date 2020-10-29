package com.example.backend.vo;

import lombok.Data;

@Data
public class ResponseVO {
    /**
     * 调用是否成功
     */
    private Boolean ret;
    /**
     * 内容
     */
    private Object content;

    public static ResponseVO buildSuccess() {
        ResponseVO response = new ResponseVO();
        response.setRet(true);
        return response;
    }

    public static ResponseVO buildSuccess(Object content) {
        ResponseVO response = new ResponseVO();
        response.setContent(content);
        response.setRet(true);
        return response;
    }

    public static ResponseVO buildFailure() {
        ResponseVO response = new ResponseVO();
        response.setRet(false);
        return response;
    }
}
