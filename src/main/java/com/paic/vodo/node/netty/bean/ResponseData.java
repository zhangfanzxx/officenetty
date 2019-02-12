package com.paic.vodo.node.netty.bean;

import java.io.Serializable;

public final class ResponseData implements Serializable {
    private static final String FAIL = "404";
    private static final String SUCCESS = "200";

    private boolean result;


    private String code;

    private Object data;

    private String msg;

    public ResponseData(Object msg) {
        if ("OK".equals(msg)) {
            this.msg = msg.toString();
            this.code = ResponseData.SUCCESS;
            this.result = true;
        } else if (msg instanceof Throwable) {
            this.msg = ((Throwable) msg).getMessage();
            this.code = FAIL;
            this.result = false;
        } else {
            this.result = true;
            this.data = msg;
            this.code = SUCCESS;
            this.msg = "操作成功";
        }
    }

    public ResponseData() {

    }

    public static ResponseData ok() {
        return new ResponseData("OK");
    }

    public boolean isResult() {
        return result;
    }

    public ResponseData setResult(boolean result) {
        this.result = result;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ResponseData setCode(String code) {
        this.code = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResponseData setData(Object data) {
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResponseData setOK(Object list) {
        this.result = true;
        this.code = "200";
        this.data = list;
        this.msg = "OK";
        return this;
    }

    public ResponseData setError(String message) {
        this.code = "404";
        this.result = false;
        this.msg = message;
        this.data = null;
        return this;
    }

    public ResponseData setResultMsg(String message, boolean b) {
        this.code =   "101";
        this.result = b;
        this.msg = message;
        this.data = null;
        return this;
    }
}
