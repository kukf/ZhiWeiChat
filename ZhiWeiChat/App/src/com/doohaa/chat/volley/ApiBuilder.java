package com.doohaa.chat.volley;

import java.util.Map;

import android.content.Context;

import com.android.volley.Response;

import jp.naver.android.commons.net.NameValuePairList;

public class ApiBuilder<T> {
	private int method;
	private Context context;
	private String url;
	private NameValuePairList params;
	private Class<T> clazz;
	private Response.Listener<T> listener;
	private Response.ErrorListener errorListener;
	private ApiServerErrorListener<T> apiServerErrorListener;
	private CancelListener cancelListener;
	private boolean responseWithCommonError = false;

	private String tag;

	public static <T> ApiBuilder<T> get() {
		return new ApiBuilder<>();
	}

	public ApiBuilder<T> setMethod(int method) {
		this.method = method;
		return this;
	}

	public ApiBuilder<T> setContext(Context context) {
		this.context = context;
		return this;
	}

	public ApiBuilder<T> setUrl(String url) {
		this.url = url;
		return this;
	}

	public ApiBuilder<T> setParams(Map<String, Object> params) {
		if (params == null) {
			return this;
		}
		if (this.params == null) {
			this.params = new NameValuePairList();
		}

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			this.params.add(entry.getKey(), String.valueOf(entry.getValue()));
		}

		return this;
	}

	public ApiBuilder<T> setParams(NameValuePairList nameValuePrams) {
		this.params = nameValuePrams;
		return this;
	}

	public ApiBuilder<T> addParams(String key, String value) {
		if (params == null) {
			params = new NameValuePairList();
		}
		this.params.add(key, value);
		return this;
	}

	public ApiBuilder<T> setClazz(Class<T> clazz) {
		this.clazz = clazz;
		return this;
	}

	public ApiBuilder<T> setListener(Response.Listener<T> listener) {
		this.listener = listener;
		return this;
	}

	public ApiBuilder<T> setErrorListener(Response.ErrorListener errorListener) {
		this.errorListener = errorListener;
		return this;
	}

	public ApiBuilder<T> setOCErrorListener(ApiServerErrorListener apiServerErrorListener) {
		this.apiServerErrorListener = apiServerErrorListener;
		return this;
	}

	public ApiBuilder<T> setCancelListener(CancelListener cancelListener) {
		this.cancelListener = cancelListener;
		return this;
	}

	public ApiBuilder<T> setTag(String tag) {
		this.tag = tag;
		return this;
	}

	public ApiBuilder<T> setResponseWithCommonError(boolean value) {
		this.responseWithCommonError = value;
		return this;
	}

	public int getMethod() {
		return method;
	}

	public Context getContext() {
		return context;
	}

	public String getUrl() {
		return url;
	}

	public NameValuePairList getParams() {
		return params;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public Response.Listener<T> getListener() {
		return listener;
	}

	public Response.ErrorListener getErrorListener() {
		return errorListener;
	}

	public ApiServerErrorListener getOCErrorListener() {
		return apiServerErrorListener;
	}

	public CancelListener getCancelListener() {
		return cancelListener;
	}

	public String getTag() {
		return tag;
	}

	public boolean getResponseWithCommonError() {
		return responseWithCommonError;
	}
}
