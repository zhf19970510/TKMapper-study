package com.zhf.tkmapperstudy.help.result;

import com.zhf.tkmapperstudy.help.enumeration.ErrorType;
import lombok.Data;

import java.io.Serializable;


@Data
public class JsonResult implements Serializable {
    private static final long serialVersionUID = -5766977494287555486L;
    /**
     * 状态码
     */
    private String state = "200000";
    /**
     * 状态码对应的信息
     */
    private String message = "处理成功";
    /**
     * 正常数据
     */
    private Object data;
    /**
     * 数据总数
     */
    private int total;


    public JsonResult() {
    }


    public JsonResult(Object data) {
        this.data = data;
    }

    public JsonResult(Object... data) {
        this.data = data;
    }

    public JsonResult(Object data, int total) {
        this.data = data;
        this.total = total;
    }

    public JsonResult(Throwable e) {
        this.state = "0";
        this.message = e.getMessage();
    }

    public JsonResult(String state, String message) {
        this.state = state;
        this.message = message;
    }

    public static JsonResult errorMessage(String message) {
        return new JsonResult("0", message);
    }

    public static JsonResult success() {
        return new JsonResult();
    }

    public static JsonResult success(Object data) {
        return new JsonResult(data);
    }

    public static JsonResult success(Object... data) {
        return new JsonResult(data);
    }

    public static JsonResult success(Object data, int total) {
        return new JsonResult(data, total);
    }

    public static JsonResult fail(ErrorType errorType) {
        return new JsonResult(errorType.getCode(), errorType.getMessage());
    }

    public static JsonResult fail(String state1, String message1) {
        return new JsonResult(state1, message1);
    }
}



