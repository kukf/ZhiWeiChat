package com.doohaa.chat.volley.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.doohaa.chat.BuildConfig;
import com.doohaa.chat.api.ApiResultValidator;
import com.doohaa.chat.utils.OCLog;
import com.doohaa.chat.volley.ApiServerErrorListener;
import com.doohaa.chat.volley.CancelListener;
import com.doohaa.chat.volley.error.ApiServerError;
import com.doohaa.chat.volley.error.ApiServerErrorHelper;
import com.doohaa.chat.volley.error.NetworkError;

import jp.naver.android.commons.net.NameValuePairList;

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by Gson.
 */
public class GsonRequest<T> extends TaleRequest<T> {
	public static final String TAG = GsonRequest.class.getSimpleName();
	private final Gson gson = new Gson();
	private final Class<T> clazz;
	private final Map<String, String> headers;
	private final NameValuePairList params;
	private final Listener<T> listener;
	private final CancelListener cancelListener;

	private ApiServerErrorHelper errorHelper;

	private long startTimeMillis;

	public GsonRequest(Context context, int method, String url, Class<T> clazz, Map<String, String> headers,
			Listener<T> listener, ErrorListener errorListener) {
		this(context, method, url, clazz, headers, listener, errorListener, null);
	}

	public GsonRequest(Context context, int method, String url, Class<T> clazz, Map<String, String> headers,
			Listener<T> listener, ErrorListener errorListener, CancelListener cancelListener) {
		this(context, method, url, clazz, headers, null, listener, errorListener, cancelListener);
	}

	public GsonRequest(Context context, int method, String url, Class<T> clazz, Map<String, String> headers,
			NameValuePairList params, Listener<T> listener, ErrorListener errorListener,
			CancelListener cancelListener) {
		this(context, method, url, clazz, headers, params, listener, null, errorListener, cancelListener, false);
	}

	public GsonRequest(Context context, int method, String url, Class<T> clazz, Map<String, String> headers,
			NameValuePairList params, Listener<T> listener, ApiServerErrorListener<T> apiServerErrorListener,
			ErrorListener errorListener, CancelListener cancelListener, boolean responseWithCommonError) {
		super(method, url, errorListener);
		this.clazz = clazz;
		this.headers = headers;
		this.params = params;
		this.listener = listener;
		this.cancelListener = cancelListener;
		errorHelper = new ApiServerErrorHelper(context, this, apiServerErrorListener, responseWithCommonError);

		requestLog();
	}

	@Override
	public String getUrl() {
		this.startTimeMillis = System.currentTimeMillis();
		return super.getUrl();
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	private NameValuePairList getParameters() {
		return this.params;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		NameValuePairList params = getParameters();
		if (params != null && params.size() > 0) {
			return encodeParameters(params, getParamsEncoding());
		}
		return null;
	}

	private byte[] encodeParameters(NameValuePairList params, String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			for (NameValuePair nameValuePair : params) {
				encodedParams.append(URLEncoder.encode(nameValuePair.getName(), paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(nameValuePair.getValue(), paramsEncoding));
				encodedParams.append('&');
			}
			return encodedParams.toString().getBytes(paramsEncoding);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}

	@Override
	protected void deliverResponse(T response) {
		if (listener != null) {
			if (ApiResultValidator.isFail(response)) {
				errorHelper.setErrorResponse(response);
			} else {
				listener.onResponse(response);
			}
		}
	}

	@Override
	public void cancel() {
		super.cancel();
		if (isCanceled()) {
			deliverCancel();
		}
	}

	private void deliverCancel() {
		if (cancelListener != null) {
			cancelListener.onCancel();
		}
	}

	@Override
	public void deliverError(VolleyError error) {
		Gson gson = new GsonBuilder().create();
		super.deliverError(error instanceof ApiServerError ? error : new NetworkError(error));
		deliverErrorForLog(error);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			deliverResponseForLog(response);
			responseLog(response, json);
			return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	private void requestLog() {
		if (!BuildConfig.DEBUG) {
			return;
		}

		String url = Uri.decode(getOriginUrl());
		if (Method.POST == getMethod()) {
			Uri.Builder builder = Uri.parse(url).buildUpon();
			for (NameValuePair nameValuePair : getParameters()) {
				builder.appendQueryParameter(nameValuePair.getName(), nameValuePair.getValue());
			}
			url = Uri.decode(builder.build().toString());
		}

		OCLog.v(TAG, String.format("[EXECUTE URL](%s) %s", getMethodString(), url));
	}

	private void responseLog(NetworkResponse response, String json) {
		if (!BuildConfig.DEBUG) {
			return;
		}

		OCLog.v(TAG, String.format("[RESPONSE CODE] (%s)", response.statusCode));
		OCLog.v(TAG, String.format("[RESPONSE DATA] (%dms) %s", (System.currentTimeMillis() - startTimeMillis), json));
	}

	private String getMethodString() {
		return getMethod() == Method.GET ? "GET" : "POST";
	}
}
