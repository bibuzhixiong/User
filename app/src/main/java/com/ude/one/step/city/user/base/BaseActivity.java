package com.ude.one.step.city.user.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;


import com.ude.one.step.city.user.App;
import com.ude.one.step.city.user.R;
import com.ude.one.step.city.user.utils.RescourseUtils;
import com.ude.one.step.city.user.utils.TUtil;
import com.ude.one.step.city.user.widget.LoadingDialog;
import com.ude.one.step.city.user.widget.StatusBarCompat;
import com.ude.one.step.city.user.widget.StatusBarUtil;

import butterknife.ButterKnife;

;

/**
 * Created by lan on 2017/4/25.
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {
    protected P mPresenter;
    protected Context mContext;
    protected Toolbar mCommonToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.bind(this);
        //状态栏颜色
//        SetStatusBarColor();
        //适配4.4状态栏
//        setTranslucentStatus(true);
//        setStatusBarColor(true);
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        if(mPresenter!=null){
            mPresenter.mContext=this;
            mPresenter.setV(this);
        }
        mCommonToolbar = ButterKnife.findById(this, R.id.common_toolbar);
        if (mCommonToolbar != null) {

            setSupportActionBar(mCommonToolbar);
            initToolBar();
        }
        this.initView();
        App.getApp().addActivity(this);

    }
    /*********************子类实现*****************************/
    //获取布局文件
    protected abstract int getLayoutId();
    //初始化ActionBar
    protected abstract void initToolBar();
    //初始化view
    protected abstract void initView();

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(){
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.main_color));
    }

    /**
     * 设置状态栏
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }
    /**
     * 设置状态栏的颜色
     */
    public void setStatusBarColor(boolean on) {
        if (on) {
            StatusBarUtil.setColor(this, RescourseUtils.getColor(R.color.main_color), 0);
        }
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
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    /**
     * 关闭Activity
     * @param activity
     */
    protected void finish_Activity(Activity activity){
        activity.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    /**
     * 开启浮动加载进度条
     */
    protected void startProgressDialog() {
        LoadingDialog.showDialogForLoading(this);
    }

    /**
     * 开启浮动加载进度条
     *
     * @param msg
     */
    protected void startProgressDialog(String msg) {
        LoadingDialog.showDialogForLoading(this, msg, true);
    }

    /**
     * 停止浮动加载进度条
     */
    protected void stopProgressDialog() {
        LoadingDialog.cancelDialogForLoading();
    }
    /**
     * 重写onKeyDown方法，点击物理键返回时，杀掉当前Activity
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish_Activity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        ButterKnife.unbind(this);
        App.getApp().finishActivity(this);
    }
}
