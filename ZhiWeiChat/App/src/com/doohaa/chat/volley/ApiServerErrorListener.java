package com.doohaa.chat.volley;

public interface ApiServerErrorListener<T> {
	/**
	 * Called when a response is received.
	 */
	public void onResponse(T response);
}
