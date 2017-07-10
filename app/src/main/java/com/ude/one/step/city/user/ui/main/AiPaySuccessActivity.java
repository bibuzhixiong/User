package com.ude.one.step.city.user.ui.main;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ude.one.step.city.user.Constant;
import com.ude.one.step.city.user.R;
import com.ude.one.step.city.user.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by ude on 2017/7/4.
 */

public class AiPaySuccessActivity extends BaseActivity {
    @Bind(R.id.webView)
    WebView webView;
    @Override
    protected int getLayoutId() {
        return R.layout.ativity_aipay_success;
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initView() {
        final WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setSupportZoom(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用网络
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);            //设置webview可缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);      //缓存模式
        settings.setAppCacheMaxSize(0); // 设置缓冲大小，8M
        settings.setAllowFileAccess(false);            // 设置使用缓存
        // 启用javascript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Constant.PAY_SUCCESS_URL);
//        webView.addJavascriptInterface(AiPaySuccessActivity.this,"android");
    }
}
