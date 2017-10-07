package com.doohaa.chat.volley.error;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.doohaa.chat.IMApplication;
import com.doohaa.chat.R;

public class NetworkError extends VolleyError {
	private VolleyError error;

	public NetworkError(VolleyError error) {
		super(error.networkResponse);
		this.error = error;
	}

	@Override
	public long getNetworkTimeMs() {
		if (error == null) {
			return -1;
		}
		return error.getNetworkTimeMs();
	}

	@Override
	public String getMessage() {
		String message = null;
		if (error instanceof TimeoutError) {
			message = "Network connection time out";

		} else if (error instanceof ParseError) {
			message = IMApplication.getInstance().getString(R.string.message_common_error);

		} else if (error instanceof ServerError) {
			message = IMApplication.getInstance().getString(R.string.message_common_error);

		} else if (error instanceof NoConnectionError) {
			message = IMApplication.getInstance().getString(R.string.Network_error);

		} else if (error instanceof com.android.volley.NetworkError) {
			handleMessage(error.networkResponse);
		} else {
			message = IMApplication.getInstance().getString(R.string.message_common_error);
		}

		return message;
	}

	private String handleMessage(NetworkResponse networkResponse) {
		if (networkResponse == null) {
			return IMApplication.getInstance().getString(R.string.message_common_error);
		}
		int statusCode = error.networkResponse == null ? -1 : error.networkResponse.statusCode;

		String message = IMApplication.getInstance().getString(R.string.message_common_error);

		return message;
	}

	@Override
	public String toString() {
		return String.format("%s, %s", this.getClass().getSimpleName(), getMessage());
	}
}
