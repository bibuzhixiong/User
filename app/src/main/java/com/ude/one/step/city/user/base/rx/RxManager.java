package com.ude.one.step.city.user.base.rx;


import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lan on 2017/4/25.
 */
public class RxManager {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者者
    private Subscriber mSubscriber;


    public void addEveny(Observable observable, Subscriber subscriber) {
        mCompositeSubscription.add(
                observable.subscribe(subscriber));
    }
    public void add(Observable observable, Subscriber subscriber) {
        mSubscriber=subscriber;
        mCompositeSubscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber));
    }
    public void clear() {
        mCompositeSubscription.unsubscribe();// 取消订阅
    }


}
