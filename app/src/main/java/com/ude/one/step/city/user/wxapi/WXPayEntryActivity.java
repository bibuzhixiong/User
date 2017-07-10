package com.ude.one.step.city.user.wxapi;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ude.one.step.city.user.Constant;
import com.ude.one.step.city.user.R;
import com.ude.one.step.city.user.base.BaseActivity;
import com.ude.one.step.city.user.utils.ToastUtils;


import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.OnClick;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	TextView tv_type1;
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;


	@Override
	protected int getLayoutId() {
		return R.layout.ativity_aipay_success;
	}

	@Override
	protected void initToolBar() {

	}

	@Override
	protected void initView() {
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}
	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
//		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		//打印一下有没有j


		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			int errCode=resp.errCode;

			if(errCode==0){

			}else if(errCode==-1){
				ToastUtils.showSingleToast("支付失败");

				finish();
			}else if(errCode==-2){
				ToastUtils.showSingleToast("用户取消了支付");
				finish();
			}

		}

	}


}