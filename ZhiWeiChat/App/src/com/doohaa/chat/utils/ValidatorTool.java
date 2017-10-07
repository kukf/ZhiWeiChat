package com.doohaa.chat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunshixiong on 6/27/16.
 */
public class ValidatorTool {
    /**
     * 验证邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 验证手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        flag = mobiles.length() == 11;
        return flag;
    }

    /**
     * 验证code
     */
    public static boolean isCode(String mobiles) {
        boolean flag = false;
        flag = mobiles.length() == 6;
        return flag;
    }

    /**
     * 验证密码
     */
    public static boolean isPassWord(String mobiles) {
        boolean flag = false;
        flag = mobiles.length() > 5;
        return flag;
    }
}
