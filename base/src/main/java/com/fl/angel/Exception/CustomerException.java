package com.fl.angel.Exception;

import com.fl.angel.base.ResponseCode;

/**
 * @Author fdm
 * @Date 2020/11/11 9:55
 * @description： 自定义异常
 */
public class CustomerException  extends RuntimeException {

    private static final long serialVersionUID = -5701182284190108797L;

    private ResponseCode code;

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public ResponseCode getCode() {
        return code;
    }

    public CustomerException() {
        super("");
    }

    public CustomerException(String message) {
        super(message);
    }

    public CustomerException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public CustomerException(ResponseCode code, String message) {
        super(message);
        this.code = code;
    }
}
