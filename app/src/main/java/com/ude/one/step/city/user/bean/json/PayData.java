package com.ude.one.step.city.user.bean.json;

/**
 * Created by ude on 2017/7/4.
 */
public class PayData<T> {
    private int status;
    private String sign;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PayData{" +
                "status=" + status +
                ", sign='" + sign + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
