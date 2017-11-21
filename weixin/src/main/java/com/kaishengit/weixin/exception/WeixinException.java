package com.kaishengit.weixin.exception;

/**
 * @author zhao
 */
public class WeixinException extends RuntimeException {

    public WeixinException() {}

    public WeixinException(String message) {
        super(message);
    }

    public WeixinException(Throwable th) {
        super(th);
    }

    public WeixinException(String message, Throwable th) {
        super(message, th);
    }

}
