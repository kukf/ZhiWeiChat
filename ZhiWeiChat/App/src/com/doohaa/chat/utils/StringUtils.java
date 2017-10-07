/*
 * StringUtils.java
 *
 * Copyright 2007 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.doohaa.chat.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nts
 */
public class StringUtils {
    private static final String TAG = "StringUtils";

    public static final String EMPTY = "";

    /**
     * @author jumahn
     */
    public static String ensureNotNull(String value) {
        return value == null ? "" : value;
    }

    public static String addCommas(float num) {
        NumberFormat formatter = new DecimalFormat("###,###");
        return formatter.format(num);
    }

    public static boolean isBlank(CharSequence s) {
        if (isEmpty(s)) {
            return true;
        }

        final int len = s.length();
        char ch;
        for (int i = 0; i < len; ++i) {
            ch = s.charAt(i);
            switch (ch) {
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence s) {
        return !isBlank(s);
    }

    public static boolean isEmpty(CharSequence s) {
        return (s == null) ? true : (s.length() == 0);
    }

    public static boolean isNotEmpty(CharSequence s) {
        return !isEmpty(s);
    }

    public static boolean isNumeric(String s) {
        if (isEmpty(s)) {
            return false;
        }

        final int len = s.length();
        char ch;
        for (int i = 0; i < len; ++i) {
            ch = s.charAt(i);
            switch (ch) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public static boolean isTrimEmpty(CharSequence str) {
        if (str == null || str.length() == 0 || str.toString().trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String trimUni(String s){
        int len = s.length();
        int st = 0;
        char[] val = s.toCharArray();

        while (st < len && (val[st] <= ' ' || val[st] == '　')) {
            st++;
        }
        while (st < len && (val[len - 1] <= ' ' || val[len - 1] == '　')) {
            len--;
        }

        if(st > 0 || len < s.length()) {
            return s.substring(st, len);
        }

        return s;
    }

    /**
     * @author jumahn
     */
    public static boolean isDigitsOnlyIncludeNegativeNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }

        if (str.startsWith("-") && str.length() > 1) {
            str = str.substring(1);
        }

        return TextUtils.isDigitsOnly(str);
    }

    /**
     * @author jumahn
     */
    public static boolean isDigitsOnly(String str) {
        if (isEmpty(str)) {
            return false;
        }

        return TextUtils.isDigitsOnly(str);
    }

    /**
     * 전달받은 String의 개행문자를 공백으로 변환하여 반환
     *
     * @param str
     * @return String
     * @author Jinsoo Jang
     */
    public static String replaceNewLineToSpace(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\n", " ");
    }

    /**
     * 전달받은 String의 Byte 길이를 반환 ( EUC-KR )
     *
     * @param str
     * @return
     */
    public static int getByteLengthToInt(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        str = str.trim();

        int byteLength = 0;
        try {
            byteLength = str.getBytes("EUC-KR").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return byteLength;
    }

    @SuppressWarnings("rawtypes")
    public static String mapToString(Map map) {
        if (map == null) {
            throw new IllegalArgumentException("empty argument.");
        }

        String result = new JSONObject(map).toString();
        return result;
    }

    public static String getStackTrace(Throwable aThrowable) {
        if (aThrowable == null) {
            return "";
        }
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    public static String nameValuePairsToUrl(List<NameValuePair> parameters) {
        StringBuffer sb = new StringBuffer();

        if (parameters == null) {
            return "";
        }

        for (NameValuePair nameValuePair : parameters) {
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append(nameValuePair.getName()).append("=").append(nameValuePair.getValue());
        }

        return sb.toString();
    }

    public static String nullToEmpty(String value) {
        if (null == value) {
            return "";
        }
        return value;
    }

    public static String arrayToString(String[] array, String saperator) {
        StringBuffer sb = new StringBuffer();
        if (array == null || array.length == 0) {
            return "";
        }

        // 배열의 첫번째 데이터
        sb.append(array[0]);

        // 배열의 두번째 데이터 부터는 saperator로 이어붙이기
        if (array.length > 1) {
            for (int i = 1; i < array.length; i++) {
                sb.append(saperator);
                sb.append(array[i]);
            }
        }

        return sb.toString();
    }

    /**
     * @param context
     * @param text
     * @param resId
     * @return
     */
    public static SpannableString changeBracketTextToImage(Context context, String text, int resId) {
        SpannableString result = new SpannableString(text);
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher match = pattern.matcher(text);

        while (match.find()) {
            int start = match.start();
            int end = match.end();

            String temp = match.group(1);
            while (temp.contains("(")) {
                start = start + temp.indexOf("(") + 1;
                temp = temp.substring(temp.indexOf("(") + 1);
            }

            ImageSpan span = new ImageSpan(context, resId, ImageSpan.ALIGN_BASELINE);
            result.setSpan(span, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return result;
    }

}
