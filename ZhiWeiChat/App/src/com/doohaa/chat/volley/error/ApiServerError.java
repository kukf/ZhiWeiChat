package com.doohaa.chat.volley.error;

import com.android.volley.VolleyError;
import com.doohaa.chat.api.dto.ApiResponse;

public class ApiServerError extends VolleyError {
	private ApiResponse response;

	public ApiServerError(ApiResponse response) {
		this.response = response;
	}

	@Override
	public String getMessage() {

		return response == null ? "" : response.getMessage();
	}

	public String getStatusCode() {
		return response == null ? "-1" : response.getCode();
	}

	@Override
	public String toString() {
		return String.format("%d : %s", getStatusCode(), getMessage());
	}
}
