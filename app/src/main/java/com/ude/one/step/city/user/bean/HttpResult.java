package com.ude.one.step.city.user.bean;

/**
 * Created by lan on 2017/3/6.
 */
public class HttpResult<T> {
    private int ResultState;
    private String Message;
    private T ReturnValue;

    public int getResultState() {
        return ResultState;
    }

    public void setResultState(int resultState) {
        ResultState = resultState;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getReturnValue() {
        return ReturnValue;
    }

    public void setReturnValue(T returnValue) {
        ReturnValue = returnValue;
    }
}
