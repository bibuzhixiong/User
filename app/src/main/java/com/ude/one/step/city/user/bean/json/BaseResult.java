package com.ude.one.step.city.user.bean.json;

/**
 * Created by ude on 2017/7/4.
 */

public class BaseResult<T> {
    private int status;
    private Info info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
