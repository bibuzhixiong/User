package com.ude.one.step.city.user.bean.json;

/**
 * Created by ude on 2017/7/4.
 */

public class Info {
    private String AppID;
    private String AppSecret;

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getAppSecret() {
        return AppSecret;
    }

    public void setAppSecret(String appSecret) {
        AppSecret = appSecret;
    }

    @Override
    public String toString() {
        return "Info{" +
                "AppID='" + AppID + '\'' +
                ", AppSecret='" + AppSecret + '\'' +
                '}';
    }
}
