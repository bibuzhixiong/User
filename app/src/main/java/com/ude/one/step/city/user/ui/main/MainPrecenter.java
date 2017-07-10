package com.ude.one.step.city.user.ui.main;

import android.util.Log;

import com.ude.one.step.city.user.api.Api;
import com.ude.one.step.city.user.base.rx.RxApiCallback;
import com.ude.one.step.city.user.base.rx.RxSubscriberCallBack;
import com.ude.one.step.city.user.bean.WXPayData;
import com.ude.one.step.city.user.bean.json.BaseResult;
import com.ude.one.step.city.user.bean.json.PayData;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by ude on 2017/7/4.
 */

public class MainPrecenter extends MainContract.Presenter{
    @Override
    protected void pay(Map<String, RequestBody> bodyMap) {
        mView.showLoading();
        mRxManager.add(Api.getInstance().pay(bodyMap),new RxSubscriberCallBack<PayData<WXPayData>>(new RxApiCallback<PayData<WXPayData>>() {
            @Override
            public void onSuccess(PayData<WXPayData> model) {
                mView.hideLoading();
                if(model.getStatus()==0){
                    mView.paySuccess(model);
                } else{
                    mView.payFail("服务器连接失败");
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.hideLoading();
                mView.payFail(msg);
            }
        }));
    }

    @Override
    protected void getAppID(Map<String, String> bodyMap) {
        mRxManager.add(Api.getInstance().getAppID(bodyMap),new RxSubscriberCallBack<BaseResult>(new RxApiCallback<BaseResult>() {
            @Override
            public void onSuccess(BaseResult model) {
                Log.e("JJJJ",model.toString());
                mView.hideLoading();
                if(model.getStatus()==0){
                    mView.getAppIDSuccess(model);
                }else{
                    mView.getAppIDFail("失败");
                }

            }

            @Override
            public void onFailure(int code, String msg) {
                mView.hideLoading();
                mView.getAppIDFail(msg);
            }
        }));
    }
}
