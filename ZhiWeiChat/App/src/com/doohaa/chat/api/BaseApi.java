package com.doohaa.chat.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.doohaa.chat.IMApplication;
import com.doohaa.chat.R;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.utils.NetworkStateUtil;
import com.doohaa.chat.volley.ApiBuilder;
import com.doohaa.chat.volley.MyVolley;
import com.doohaa.chat.volley.request.GsonRequest;

import jp.naver.android.commons.lang.StringUtils;
import jp.naver.android.commons.net.NameValuePairList;

public class BaseApi {

    protected static final String API_DOMAIN = Config.API_DOMAIN;

    public static final int INITIAL_TIMEOUT_MS = 4000;
    public static final int MAX_NUM_RETRIES = 0;

    public static int timeoutMs = 0;
    public static int retryCount = -1;

    public static <T> void getResponse(Context context, String url, Class<T> clazz, Response.Listener<T> listener) {
        getResponse(context, url, null, clazz, listener, null);
    }

    public static <T> void getResponse(Context context, String url, Map<String, Object> params, Class<T> clazz,
                                       Response.Listener<T> listener) {
        getResponse(context, url, params, clazz, listener, null);
    }

    public static <T> void getResponse(Context context, String url, Class<T> clazz, Response.Listener<T> listener,
                                       Response.ErrorListener errorListener) {
        getResponse(context, url, null, clazz, listener, errorListener);
    }

    public static <T> void getResponse(Context context, String url, Map<String, Object> params, Class<T> clazz,
                                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        ApiBuilder<T> apiBuilder = ApiBuilder.get();
        apiBuilder.setUrl(url)
                .setContext(context)
                .setParams(params)
                .setMethod(Method.GET)
                .setClazz(clazz)
                .setListener(listener)
                .setErrorListener(errorListener);

        execute(apiBuilder);
    }

    public static <T> void postResponse(Context context, String url, Map<String, Object> params, Class<T> clazz,
                                        Response.Listener<T> listener, Response.ErrorListener errorListener) {

        ApiBuilder<T> apiBuilder = ApiBuilder.get();
        apiBuilder.setUrl(url)
                .setContext(context)
                .setParams(params)
                .setMethod(Method.POST)
                .setClazz(clazz)
                .setListener(listener)
                .setErrorListener(errorListener);

        execute(apiBuilder);
    }

    public static <T> void execute(ApiBuilder<T> apiBuilder) {
        Request<T> request = createRequest(apiBuilder);

        if (StringUtils.isNotBlank(apiBuilder.getTag())) {
            request.setTag(apiBuilder.getTag());

        } else if (apiBuilder.getContext() != null) {
            request.setTag(apiBuilder.getContext().getClass().getSimpleName());
        }

        executeRequest(apiBuilder.getContext(), request);
    }

    private static <T> void executeRequest(Context context, Request<T> request) {
        if (isNetworkUnAvailable(context)) {
            request.deliverError(new NoConnectionError());
            Toast.makeText(context, context.getResources().getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
            return;
        }

        MyVolley.getRequestQueue().add(request);

    }

    private static <T> Request<T> createRequest(ApiBuilder<T> apiBuilder) {
        String url = apiBuilder.getUrl();

        apiBuilder.setParams(appendDefaultParams(apiBuilder.getParams(), apiBuilder.getContext()));

        if (apiBuilder.getMethod() == Method.GET && apiBuilder.getParams() != null) {
            url = buildUrl(apiBuilder.getUrl(), apiBuilder.getParams());
        }
        timeoutMs = timeoutMs > 0 ? timeoutMs : INITIAL_TIMEOUT_MS;
        retryCount = retryCount > -1 ? retryCount : MAX_NUM_RETRIES;

        Request<T> request = new GsonRequest<>(apiBuilder.getContext(), apiBuilder.getMethod(), url, apiBuilder.getClazz(), buildHeaders(apiBuilder.getContext()), apiBuilder.getParams(),
                apiBuilder.getListener(), apiBuilder.getOCErrorListener(), apiBuilder.getErrorListener(), apiBuilder.getCancelListener(), apiBuilder.getResponseWithCommonError());
        request.setRetryPolicy(new DefaultRetryPolicy(timeoutMs, retryCount, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return request;
    }

    public static String getURLEncode(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        try {
            return URLEncoder.encode(content, "utf-8"); // UTF-8
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

    private static boolean isNetworkUnAvailable(Context context) {
        return NetworkStateUtil.isNetworkAvailable(context) == false;
    }

    private static Map<String, String> buildHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", TokenPreferences.getInstance(IMApplication.getInstance()).getToken());
        return headers;
    }

    protected static String buildUrl(String url, Map<String, Object> params) {
        if (url == null || url.length() <= 0) {
            throw new IllegalArgumentException("url is null!!!");
        }
        if (params == null) {
            return url;
        }

        Uri.Builder builder = Uri.parse(url).buildUpon();

        for (String key : params.keySet()) {
            builder.appendQueryParameter(key, String.valueOf(params.get(key)));
        }

        return builder.build().toString();
    }

    private static String buildUrl(String url, NameValuePairList nameValuePairList) {
        if (url == null || url.length() <= 0) {
            throw new IllegalArgumentException("url is null!!!");
        }
        if (nameValuePairList == null) {
            return url;
        }

        Uri.Builder builder = Uri.parse(url).buildUpon();

        for (NameValuePair nameValuePair : nameValuePairList) {
            builder.appendQueryParameter(nameValuePair.getName(), nameValuePair.getValue());
        }

        return builder.build().toString();
    }

    private static Map<String, Object> appendDefaultParams(Map<String, Object> params, Context context) {
        if (params == null) {
            params = new HashMap<>();
        }

        //		UserPreferences userPref = UserPreferences.getInstance(context);
        //		if (userPref.isValidUser() && userPref.getUser().getUser() != null) {
        //			params.put(KEY_USER_ID, userPref.getUser().getUser().getUserId());
        //			params.put(KEY_USER_TOKEN, userPref.getUser().getUserToken());
        //		}

        return params;
    }

    private static NameValuePairList appendDefaultParams(NameValuePairList params, Context context) {
        if (params == null) {
            params = new NameValuePairList();
        }
        //		UserPreferences userPref = UserPreferences.getInstance(context);
        //		if (userPref.isValidUser() && userPref.getUser().getUser() != null) {
        //			params.add(KEY_USER_ID, String.valueOf(userPref.getUser().getUser().getUserId()));
        //			params.add(KEY_USER_TOKEN, userPref.getUser().getUserToken());
        //		}

        return params;
    }

}
