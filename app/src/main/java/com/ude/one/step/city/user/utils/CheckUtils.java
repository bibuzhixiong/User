package com.ude.one.step.city.user.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lan on 2017/4/25.
 */
public class CheckUtils {
    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    /**
     * 验证手机号金额格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isMoney(String mobiles) {
        Pattern p = Pattern
                .compile("^([1-9]\\d{0,9}|0)([.]?|(\\.\\d{1,2})?)$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }
    /**
     * 验证密码:是否为数字、字母、下划线
     * @param pwd
     * @return
     */
    public static boolean isPwd(String pwd){
        Pattern p=Pattern.compile("^[A-Za-z0-9_]{6,20}$");
        Matcher m=p.matcher(pwd);
        return m.matches();
    }

    /**
     * 验证是否为字符串
     * @param str
     * @return
     */
    public static boolean isString(String str){
        Pattern p=Pattern.compile("^[\u4E00-\u9FA5A-Za-z0-9 ]+$");
        Matcher m=p.matcher(str);
        return m.matches();
    }
    /**
     * 验证是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }
    /*
    * 验证身份证格式
    *
    * */
    public static boolean isIdCardNum(String str) {
        Pattern pattern = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }




    /**
     * 校验银行卡卡号
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }



}
