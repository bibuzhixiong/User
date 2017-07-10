package com.ude.one.step.city.user;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.ude.one.step.city.user.service.LocationService;

import java.util.Stack;

/**
 * Created by ude on 2017/6/30.
 */

public class App extends Application {

    private String latitude="";
    private String longitude="";
    private static App context;
    private static App mApp;

    // 运用list来保存们每一个activity是关键
    public static Stack<Activity> activityStack;

    public static LocationService locationService,mylocationService;
    public  static Vibrator mVibrator;

    private static final String TAG = "JPush";
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        Log.e("TTT","111");

        //百度地图服务
        locationService = new LocationService(context);
        mVibrator =(Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(context);





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
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            Log.e("HHH","有反应"+sb.toString());

            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                String
                        latitude1 = location.getLatitude() + "";
                String  longitude1 = location.getLongitude() + "";

                App app=App.getApp();
                app.setLatitude(latitude1);
                app.setLongitude(longitude1);
                Log.e("HHH",latitude1+"--"+longitude1+"--"+app.getLatitude());
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };
    //初始化百度地图信息，不能喝极光同时启动，暂时没有解决问题
   /* public static void init(){
        *//***
     * 初始化定位sdk，建议在Application中创建
     *//*
        locationService = new LocationService(context);

        mVibrator =(Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
       SDKInitializer.initialize(context);
    }*/

 /*   public static void getJpush(Context context){
        JPushInterface.init(context);
    }*/
    public static App getApp()
    {
        if (null == mApp) {
            mApp = new App();
        }
        return mApp;
    }
    public static Context getAppContext() {
        return context;
    }
    public static Resources getAppResources() {
        return context.getResources();
    }
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }
    //把所有Activity添加到集合
    public void addActivity(Activity activity){
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();



    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public static App getContext() {
        return context;
    }

    public static void setContext(App context) {
        App.context = context;
    }

    public static App getmApp() {
        return mApp;
    }

    public static void setmApp(App mApp) {
        App.mApp = mApp;
    }
}

