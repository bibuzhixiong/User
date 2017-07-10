package com.ude.one.step.city.user.base.rx;

/**
 * Created by lan on 2016/7/4.
 */
public interface RxApiCallback<T> {
    void onSuccess(T model);

    void onFailure(int code, String msg);

}
