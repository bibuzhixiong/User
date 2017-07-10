package com.ude.one.step.city.user.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ude.one.step.city.user.App;
import com.ude.one.step.city.user.Constant;
import com.ude.one.step.city.user.R;
import com.ude.one.step.city.user.base.BaseActivity;
import com.ude.one.step.city.user.bean.WXPayData;
import com.ude.one.step.city.user.bean.json.BaseResult;
import com.ude.one.step.city.user.bean.json.PayData;
import com.ude.one.step.city.user.service.LocationService;
import com.ude.one.step.city.user.ui.aipay.PayResult;
import com.ude.one.step.city.user.utils.SPUtils;
import com.ude.one.step.city.user.utils.ToastUtils;
import com.ude.one.step.city.user.utils.WechatShareManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends BaseActivity<MainPrecenter> implements MainContract.View {
    private LocationService locationService;
    private final int BAIDU_READ_PHONE_STATE=1;
    WebView mWebView;
    private String functionName;
    private IWXAPI api;

    Bitmap thmb=null;
    private String isAiOrWXPay="";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initToolBar() {

    }
    @Override
    protected void initView() {

//        mWebView = (WebView) findViewById(R.id.webView);
        mWebView=new WebView(this);

        final WebSettings settings = mWebView.getSettings();
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
//        settings.setSupportZoom(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWebView.setWebContentsDebuggingEnabled(true);
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
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(MainActivity.this,"android");


//      mWebView.loadUrl("http://onestepcity.c.hiiyee.com/oc.html");

        final int version= Build.VERSION.SDK_INT;
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url,final String message, JsResult result) {
                Log.d("main", "onJsAlert:" + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("提示")
                                .setMessage(message)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        mWebView.reload();//重写刷新页面
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();

                    }
                });
                result.confirm();//这里必须调用，否则页面会阻塞造成假死
                return true;
            }
        });

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;// true表示此事件在此处被处理，不需要再广播
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl("javascript:initAppReady('android')");
                super.onPageFinished(view, url);
            }
        });
//        mWebView.loadUrl("file:///android_asset/gg.html");
        mWebView.loadUrl("http://ude.1ybtc.com/");
        setContentView(mWebView);
        //加载微信key
        Map map=new HashMap<>();
        map.put("isClient","User");
        mPresenter.getAppID(map);
    }
    //由于安全原因 需要加 @JavascriptInterface
    @JavascriptInterface
    public void getLocation(final String fuction){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(MainActivity.this,"location",Toast.LENGTH_LONG).show();
                functionName=fuction;
                startLocation();

            }
        });
    }
    //由于安全原因 需要加 @JavascriptInterface
    @JavascriptInterface
    public void jsLocation( final  String latitude1,final  String longitude1){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:"+functionName+"("+latitude1+","+longitude1+")");

            }
        });
    }
    //由于安全原因 需要加 @JavascriptInterface
    @JavascriptInterface
    public void paySuccess(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.e("HHH",Constant.PAY_SUCCESS_URL+"---成功");
                mWebView.loadUrl("javascript:"+Constant.PAY_SUCCESS_URL+"()");
            }
        });
    }

    //由于安全原因 需要加 @JavascriptInterface
    @JavascriptInterface
    public void login( final  String auth){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SPUtils.setSharedStringData(MainActivity.this,"AUTH",auth);
            }
        });
    }
    @JavascriptInterface
    public void shareWechat( final  String str,final String isType){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject=new JSONObject(str);
                    Log.e("HHH",jsonObject.toString());
                    String title= jsonObject.get("title").toString();
                    String link= jsonObject.get("link").toString();
                    String desc= jsonObject.get("desc").toString();
                    String images= jsonObject.get("images").toString();

                    if(Constant.APP_ID==null){
                        Map map=new HashMap<>();
                        map.put("isClient","User");
                        mPresenter.getAppID(map);
                        startProgressDialog("加载中...");

                    }else{
                        api = WXAPIFactory.createWXAPI(MainActivity.this,"wx6106724231022c6f", true);
                        api.registerApp("wx6106724231022c6f");
                        share(link,title,images,desc,isType);
                    }



                }catch (Exception e){

                }
            }
        });
    }

    // 微信分享
    private void share(String url,String title,String imgpath,String desc,String isType) {
        if (!api.isWXAppInstalled()) {
            ToastUtils.showSingleToast("您还未安装微信客户端");
            return;
        }
        //创建一个WebPageObject对象，并传入相对应的URL，接收相应分享的资源
//        String url="http://onestepcity.c.hiiyee.com/";
        WXWebpageObject webpageObject=new WXWebpageObject();
        webpageObject.webpageUrl=url;
        //第二步：创建WXMediaMessage对象，封装WebPageObject对象，并且设置相应的参数信息
        WXMediaMessage msg=new WXMediaMessage(webpageObject);
        msg.title=title;
        msg.description=desc;

        Glide.with(MainActivity.this).load(imgpath).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                thmb=resource;
            }
        });
        //第三步：创建一个缩略图
//         thmb= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        Bitmap thmBitmap=Bitmap.createScaledBitmap(thmb, 120, 120, true);
        msg.thumbData=bmpToByteArray(thmBitmap, true);
//        WXImageObject imgObj = new WXImageObject();
//        imgObj.imagePath="http://onestepcity.c.hiiyee.com/uploads/attach/ad/201705/5909e6e1848ce.jpg";
//        msg.mediaObject=imgObj;
        //第四步：创建一个SendMessage对象
        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=buildTransaction("webpage");
        req.message=msg;
        if(isType.equals("2")){
            req.scene=SendMessageToWX.Req.WXSceneTimeline;
        }else{
            req.scene=SendMessageToWX.Req.WXSceneSession;
        }

        // 调用api接口发送数据到微信
        api.sendReq(req);
    }

    //由于安全原因 需要加 @JavascriptInterface
    @JavascriptInterface
    public void pay( final String str,final String url){
//        Log.e("HHH",str);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ToastUtils.showSingleToast("pay--"+str);
                try {
                    JSONObject jsonObject=new JSONObject(str);
//                    Log.e("HHH",jsonObject.toString());
                    String orderNo= jsonObject.get("orderNo").toString();
                    String payType= jsonObject.get("isType").toString();
                    String orderType= jsonObject.get("orderType").toString();
                    Map<String, RequestBody> bodyMap = new HashMap<>();
                    bodyMap.put("auth", RequestBody.create(
                            MediaType.parse("multipart/form-data"), SPUtils.getSharedStringData(MainActivity.this,"AUTH")));
                    bodyMap.put("orderNo", RequestBody.create(
                            MediaType.parse("multipart/form-data"), orderNo));
                    bodyMap.put("isType", RequestBody.create(
                            MediaType.parse("multipart/form-data"), payType));
                    bodyMap.put("isClient", RequestBody.create(
                            MediaType.parse("multipart/form-data"),"User"));
                    bodyMap.put("orderType", RequestBody.create(
                            MediaType.parse("multipart/form-data"), orderType));
                    isAiOrWXPay=payType;
                    Constant.PAY_SUCCESS_URL=url;
                    mPresenter.pay(bodyMap);
                }catch (Exception e){

                }

            }
        });
    }
    private void startLocation(){
        //自动开始定位
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            locationService.start();// 定位SDK
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // -----------location config ------------
        //百度地图服务
        locationService = ((App) getApplication()).locationService;
        locationService.registerListener(myListener);

        }

    public void showContacts(){
//        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, BAIDU_READ_PHONE_STATE);
        }else{
            locationService.start();// 定位SDK
//            init();
        }
    }
    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:


                Log.e("HHH",grantResults[0]+"---"+PackageManager.PERMISSION_GRANTED);
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
//                    init();
                    locationService.start();// 定位SDK
                    // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request

                } else {
                    // 没有获取到权限，做特殊处理,关掉app
                    App.getApp().AppExit(this);
                    App.getApp().finishAllActivity();
                    finish();
//                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuilder sb=new StringBuilder();
            if (location.getLocType() == BDLocation.TypeServerError) {
                jsLocation("0","0");
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                locationService.stop();
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                jsLocation("0","0");
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
                locationService.stop();
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                jsLocation("0","0");
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                locationService.stop();
            }
            Log.e("HHH","有反应"+sb.toString());

            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                String
                        latitude1 = location.getLatitude() + "";
                String  longitude1 = location.getLongitude() + "";

               jsLocation(latitude1,longitude1);
                Log.e("HHH",latitude1+"--"+longitude1+"--");
                locationService.stop();
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };
        //支付宝支付
    private void androidPay(final String orderInfo){

        Log.e("pay",orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MainActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private void androidWxPay(WXPayData data){
        Constant.APP_ID=data.getAppid();
        api = WXAPIFactory.createWXAPI(MainActivity.this, Constant.APP_ID);
        api.registerApp(Constant.APP_ID);

        if (!api.isWXAppInstalled()) {
            //提醒用户没有安装微信
            Toast.makeText(MainActivity.this, "没有安装微信,请先安装微信!", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isPaySupported = api.getWXAppSupportAPI() >= com.tencent.mm.opensdk.constants.Build.PAY_SUPPORTED_SDK_INT;
        if(!isPaySupported){
            ToastUtils.showSingleToast("您手机安装微信不支持支付！");
            return;
        }
        PayReq req = new PayReq();
        req.appId			=data.getAppid();
        req.partnerId		= data.getPartnerid();
        req.prepayId		= data.getPrepayid();
        req.nonceStr		= data.getNoncestr();
        req.timeStamp		= data.getTimestamp();
        req.packageValue	= "Sign=WXPay";
        req.sign			= data.getSign();
//        req.extData			= "app data";
//        Log.e("GGG",data.getAppid()+"--"+data.getPartnerid()+"--"+data.getPrepayid()+"--"+data.getNoncestr()+"--"+data.getTimestamp()+"--"+data.getSign());
        api.sendReq(req);
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                Toast.makeText(RechargeActivity.this, "正常调起支付"+req.checkArgs(), Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        paySuccess();
//                        Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        };
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
            else{

                finish();

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void paySuccess(PayData<WXPayData> model) {

        if(isAiOrWXPay.equals("1")){
            androidWxPay(model.getData());
        }else{
            pay(model.getSign());
        }
    }
    private void pay(final String orderInfo){


        Log.e("pay",orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MainActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void payFail(String msg) {
    }
    @Override
    public void showLoading() {
            startProgressDialog("正在加载中....");
        WechatShareManager.getInstance(MainActivity.this).getShareContentText("sfsf");
    }

    @Override
    public void hideLoading() {
        stopProgressDialog();
    }

    @Override
    public void getAppIDSuccess(BaseResult modle) {
        Constant.APP_ID=modle.getInfo().getAppID();
    }

    @Override
    public void getAppIDFail(String msg) {
//        ToastUtils.showSingleToast(msg);

    }
    //微信分享指定类型
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    //图片压缩
    public  byte[] bmpToByteArray(final Bitmap bmp,
                                  final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 80, output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
