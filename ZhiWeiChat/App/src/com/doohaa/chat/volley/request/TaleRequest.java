package com.doohaa.chat.volley.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.doohaa.chat.api.Config;
import com.doohaa.chat.api.dto.ApiResponse;
import com.doohaa.chat.utils.StringUtils;

public abstract class TaleRequest<T> extends Request<T> {

    private NetworkResponse response;

    private boolean logEnable;
    private VolleyError error;

    public TaleRequest(int method, String url, Response.ErrorListener listener) {
        super(method, new StringBuffer(Config.API_DOMAIN).append(url).toString(), listener);

    }

    @Override
    public void addMarker(String tag) {
        super.addMarker(tag);
    }

    public void addMarker(String tag, long timeMs) {
    }

    protected void deliverErrorForLog(VolleyError error) {
        if (logEnable) {
            this.error = error;
            finish(this.toString());
        }
    }

    protected void deliverResponseForLog(NetworkResponse response) {
        this.response = response;
    }

    private String getResult() {
        if (error != null) {
            //error
            return StringUtils.isEmpty(error.getMessage()) ? error.getClass().getSimpleName() : error.getMessage();
        }

        if (response == null) {
            return null;
        }

        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            ApiResponse apiResponse = new Gson().fromJson(json, ApiResponse.class);
            response = null;
            return apiResponse.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void finish(String tag) {
        if (logEnable) {
        }
    }
}
