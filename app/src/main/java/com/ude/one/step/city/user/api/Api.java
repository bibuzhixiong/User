package com.ude.one.step.city.user.api;

import com.ude.one.step.city.user.bean.WXPayData;
import com.ude.one.step.city.user.bean.json.BaseResult;
import com.ude.one.step.city.user.bean.json.PayData;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by ude on 2017/5/17.
 */

public class Api {
    public final static String API_BASE_URL="http://api.1ybtc.com/";
    public static Api instance;
    private ApiService service;

    //添加请求头
    Interceptor mInterceptor =new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Authorization", "T25lU3RlcENJdHlUb2tlbjpkRzlyWlc0dmFXNWtaWGd2ZEdGaWZESXdNVGN3TlRBME1UQXpPQQ==");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    };



    public Api() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(mInterceptor)
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }
    public static Api getInstance() {
        if (instance == null)
            instance = new Api();
        return instance;
    }
//
    //获取订单支付
    public Observable<PayData<WXPayData>> pay(Map<String,RequestBody> bodyMap){
        return service.pay(bodyMap);
    }
    //获取订单支付
    public Observable<BaseResult> getAppID(Map<String,String> bodyMap){
        return service.getAppID(bodyMap);
    }
}
