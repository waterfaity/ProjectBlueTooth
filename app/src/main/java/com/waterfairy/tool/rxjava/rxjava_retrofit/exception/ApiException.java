package com.waterfairy.tool.rxjava.rxjava_retrofit.exception;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class ApiException extends Exception {
    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}
