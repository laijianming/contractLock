package com.ragdoll.entity;

/**
 * @author jianming
 * @create 2021-03-08-23:56
 */
public class ClientResult {


    private String msg;
    private int state;
    private Object data;

    public ClientResult() {
    }

    public ClientResult(String msg, int state, Object data) {
        this.msg = msg;
        this.state = state;
        this.data = data;
    }

    public static ClientResult ok(Object data) {
        return new ClientResult("ok", 200, data);
    }

    public static ClientResult build(String msg, int state, Object data) {
        return new ClientResult(msg, state, data);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
