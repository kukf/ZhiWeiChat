package com.doohaa.chat;

import android.content.Context;
import android.content.Intent;

import com.doohaa.chat.api.dto.ApiResponse;
import com.doohaa.chat.ui.login.LoginActivity;

public class ActivityStarter {
	public static final String CODE_APP_FINISH = "finish";
	public static final String CODE_SERVICE_ERROR = "service_error";
	public static final String CODE_SERVICE_ERROR_CODE = "service_error_code";
	public static final String CODE_SERVICE_ERROR_MESSAGE = "service_error_message";
	public static final String CODE_SERVICE_ERROR_DATA = "service_error_data";

	private static Intent intent;

	private static void initLoginActivity(Context context) {
		intent = new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	private static void initServerErrorActivity(Context context) {
		intent = new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	}

	public static void startLoginActivity(Context context) {
		initLoginActivity(context);
		context.startActivity(intent);
	}

	public static void appFinish(Context context) {
		initLoginActivity(context);
		intent.putExtra(CODE_APP_FINISH, true);
		context.startActivity(intent);
	}

	public static void startServiceErrorActivity(Context context, ApiResponse response) {
		initServerErrorActivity(context);
		intent.putExtra(CODE_SERVICE_ERROR, true);
		intent.putExtra(CODE_SERVICE_ERROR_CODE, response.getCode());
		intent.putExtra(CODE_SERVICE_ERROR_MESSAGE, response.getCode());
		context.startActivity(intent);
	}
}
