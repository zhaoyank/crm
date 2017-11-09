package com.kaishengit.util;

/**
 * @author zhao
 */
public class JsonResult {

    public static final String STATE_SUCCESS = "success";
    public static final String STATE_ERROR = "error";

    private String state;
    private String message;
    private Object data;

    public static JsonResult success() {
        return  new JsonResult(STATE_SUCCESS);
    }

    public static JsonResult success(Object data) {
        return new JsonResult(STATE_SUCCESS, data);
    }

    public static JsonResult error(String message) {
        return new JsonResult(STATE_ERROR, message);
    }

    public JsonResult() {}

    public JsonResult(String state) {
        this.state = state;
    }

    public JsonResult(String state, Object data) {
        this.state = state;
        this.data = data;
    }

    public JsonResult(String state, String message) {
        this.state = state;
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
