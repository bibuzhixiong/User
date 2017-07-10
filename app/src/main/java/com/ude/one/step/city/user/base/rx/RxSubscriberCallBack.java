package com.ude.one.step.city.user.base.rx;




import com.ude.one.step.city.user.App;
import com.ude.one.step.city.user.R;
import com.ude.one.step.city.user.utils.NetWorkUtils;
import com.ude.one.step.city.user.utils.RescourseUtils;

import rx.Subscriber;

/**
 * Created by lan on 2016/7/4.
 */
public class RxSubscriberCallBack<T> extends Subscriber<T> {
    private RxApiCallback<T> rxApiCallback;
    public RxSubscriberCallBack(RxApiCallback<T> mapiCallbackRx){
        this.rxApiCallback = mapiCallbackRx;
    }
    @Override
    public void onCompleted(){

    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        //网络
        if (!NetWorkUtils.isNetConnected(App.getAppContext())) {
            rxApiCallback.onFailure(0, RescourseUtils.getString(R.string.no_net));
        }
        //服务器
        else{
            rxApiCallback.onFailure(1, "获取数据失败");
        }

    }
    @Override
    public void onNext(T t) {
        rxApiCallback.onSuccess(t);

    }
}
