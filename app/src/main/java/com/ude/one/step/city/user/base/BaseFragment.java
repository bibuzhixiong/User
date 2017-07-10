package com.ude.one.step.city.user.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ude.one.step.city.user.R;
import com.ude.one.step.city.user.utils.TUtil;
import com.ude.one.step.city.user.widget.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by lan on 2017/4/25.
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
        protected P mPresenter;
        protected View mFragmentRootView;
        protected FragmentActivity activity;
        protected Context mContext;


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mFragmentRootView = inflater.inflate(getLayoutId(), container, false);
            activity = getSupportActivity();
            mContext = activity;
            return mFragmentRootView;

        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ButterKnife.bind(this,mFragmentRootView);
            mPresenter = TUtil.getT(this, 0);
            if(mPresenter!=null){
                mPresenter.mContext=activity;
                mPresenter.setV(this);
            }
            initView();
        }


        protected FragmentActivity getSupportActivity() {
            return super.getActivity();
        }

        protected abstract int getLayoutId();

        protected abstract void initView();
        /**
         * 开启浮动加载进度条
         */
        protected void startProgressDialog() {
            LoadingDialog.showDialogForLoading(activity);
        }

        /**
         * 开启浮动加载进度条
         *
         * @param msg
         */
        protected void startProgressDialog(String msg) {
            LoadingDialog.showDialogForLoading(activity, msg, true);
        }

        /**
         * 停止浮动加载进度条
         */
        public void stopProgressDialog() {
            LoadingDialog.cancelDialogForLoading();
        }
        /**
         * 通过Class跳转界面
         **/
        protected void startActivity(Class<?> cls) {
            startActivity(cls, null);
        }

        /**
         * 通过Class跳转界面
         **/
        protected void startActivityForResult(Class<?> cls, int requestCode) {
            startActivityForResult(cls, null, requestCode);
        }

        /**
         * 含有Bundle通过Class跳转界面
         **/
        protected void startActivityForResult(Class<?> cls, Bundle bundle,
                                              int requestCode) {
            Intent intent = new Intent();
            intent.setClass(activity, cls);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        /**
         * 含有Bundle通过Class跳转界面
         **/
        protected void startActivity(Class<?> cls, Bundle bundle) {
            Intent intent = new Intent();
            intent.setClass(activity, cls);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
            activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        @Override
        public void onDetach() {
            super.onDetach();

            this.activity = null;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.unbind(this);
        }
    }

