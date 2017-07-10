package com.ude.one.step.city.user.base;

import android.content.Context;

import com.ude.one.step.city.user.base.rx.RxManager;


/**
 * Created by lan on 2017/4/25.
 */
public abstract class BasePresenter<V> {
    public Context mContext;
    public V mView;
    public RxManager mRxManager=new RxManager();
    public void setV(V v){
        this.mView=v;
        this.onStart();
    }
    public void onDestroy(){
        this.mView=null;
        mRxManager.clear();
    }
    public abstract void onStart();
}
