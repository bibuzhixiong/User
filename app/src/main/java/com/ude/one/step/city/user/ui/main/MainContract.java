package com.ude.one.step.city.user.ui.main;

import com.ude.one.step.city.user.base.BasePresenter;
import com.ude.one.step.city.user.base.BaseView;
import com.ude.one.step.city.user.bean.WXPayData;
import com.ude.one.step.city.user.bean.json.PayData;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by ude on 2017/7/4.
 */

public interface MainContract {
    interface View extends BaseView {

        void paySuccess(PayData<WXPayData> model);
        void payFail(String msg);
        void showLoading();
        void hideLoading();
        void getAppIDSuccess(com.ude.one.step.city.user.bean.json.BaseResult modle);
        void getAppIDFail(String msg);
    }
    abstract class Presenter extends BasePresenter<View> {
        protected abstract void pay(Map<String, RequestBody> bodyMap);
        protected abstract void getAppID(Map<String, String> bodyMap);
        @Override
        public void onStart() {

        }

    }
}
