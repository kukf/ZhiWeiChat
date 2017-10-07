package com.doohaa.chat.api;

import android.content.Context;

public class Config {
	public static String API_DOMAIN;
	public static final String PLAY_STORE_SCHEME_URL = "market://details?id=com.linecorp.tale";
	public static final String PLAY_STORE_WEB_URL = "https://play.google.com/store/apps/details?id=com.linecorp.tale";

	public static void init(Context context) {
		API_DOMAIN = "http://www.jinronghui.wang:8080/";
	}
}
