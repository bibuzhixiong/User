package com.ude.one.step.city.user.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.ude.one.step.city.user.App;


/**
 * Created by lan
 * on 2016-09-19.
 * todo
 * 资源获取的工具类
 */
public class RescourseUtils {

    public static Drawable getDrawble(@DrawableRes int id){
        return ContextCompat.getDrawable(App.getAppContext(),id);
    }

    public static int getColor(@ColorRes int id){
        return ContextCompat.getColor(App.getAppContext(),id);
    }

    public static String getString(@StringRes int id){
        return  App.getAppResources().getString(id);
    }


}
