package com.fl.angel.Exception;

import com.fl.angel.base.ResponseCode;

/**
 * @Author fdm
 * @Date 2020/11/11 10:51
 * @description： 自定义服务降级异常
 */
public class FallbackException extends RuntimeException {

    private ResponseCode code = ResponseCode.SERVER_DOWNGRADE_CODE;

    public ResponseCode getCode() {
        return code;
    }

    public FallbackException(String message) {
        super(message);
    }

    public FallbackException() {
        this.code = code;
    }

}
