package com.fl.angel.base;

/**
 * @Author fdm
 * @Date 2020/11/11 9:34
 * @description：  响应返回数据工具类
 */
public class ResponseUtil {

    private ResponseUtil() {}

    public static <E> ResponseData<E> ok() {
        ResponseData<E> res = new ResponseData<E>();
        res.ok(null);
        return res;
    }

    public static <E> ResponseData<E> ok(E data) {
        ResponseData<E> res = new ResponseData<E>();
        res.ok(data);
        return res;
    }

    public static <E> ResponseData<E> ok(E data, String message) {
        ResponseData<E> res = new ResponseData<E>();
        res.ok(data, message);
        return res;
    }

    public static <E> ResponseData<E> fail(String message) {
        ResponseData<E> res = new ResponseData<E>();
        res.fail(message);
        return res;
    }

    public static <E> ResponseData<E> failByParams(String message) {
        ResponseData<E> res = new ResponseData<E>();
        res.fail(message);
        res.setCode(ResponseCode.PARAM_ERROR_CODE.getCode());
        return res;
    }

    public static <E> ResponseData<E> fail(String message, ResponseCode code) {
        ResponseData<E> res = new ResponseData<E>();
        res.fail(message, code);
        return res;
    }

    public static <E> ResponseData<E> fail(String domain, String message, ResponseCode code) {
        ResponseData<E> res = new ResponseData<E>();
        res.fail(message, code, domain);
        return res;
    }

    public static <E> ResponseData<E> fail(String domain, String message, String requestId, ResponseCode code) {
        ResponseData<E> res = new ResponseData<E>();
        res.fail(message, code, domain, requestId);
        return res;
    }

    public static <E> ResponseData<E> fail(String message, int code) {
        ResponseData<E> res = new ResponseData<E>();
        res.fail(message, code);
        return res;
    }

}
