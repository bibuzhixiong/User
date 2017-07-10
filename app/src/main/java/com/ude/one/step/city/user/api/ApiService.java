package com.ude.one.step.city.user.api;


import com.ude.one.step.city.user.bean.WXPayData;
import com.ude.one.step.city.user.bean.json.BaseResult;
import com.ude.one.step.city.user.bean.json.PayData;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by ude on 2017/5/17.
 */

public interface ApiService {
    //查询积分
//    @GET("token/data/integration")
//    Observable<BaseRows<List<MyPointsData>>> getIntegration(@QueryMap Map<String,String> map);
    //获取订单支付
    @Multipart
    @POST("token/order/pay")
    Observable<PayData<WXPayData>> pay(@PartMap Map<String, RequestBody> bodyMap);


    //版本更新
    @GET("/token/index/wechatConfig")
    Observable<BaseResult> getAppID(@QueryMap Map<String, String> bodyMap);
}