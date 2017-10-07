package com.doohaa.chat.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class OCLog {

	private final static String TAG = "ZhiWeiChat";
	private final static int LOG_TEXT_MAX_LENGTH = 4000;

	public static void v(Object message) {
		v(TAG, message);
	}

	public static void d(Object message) {
		d(TAG, message);
	}

	public static void i(Object message) {
		i(TAG, message);
	}

	public static void w(Object message) {
		w(TAG, message);
	}

	public static void e(Object message) {
		e(TAG, message);
	}

	public static void v(String tag, Object message) {
		print(Log.VERBOSE, tag, message);
	}

	public static void d(String tag, Object message) {
		print(Log.DEBUG, tag, message);
	}

	public static void i(String tag, Object message) {
		print(Log.INFO, tag, message);
	}

	public static void w(String tag, Object message) {
		print(Log.WARN, tag, message);
	}

	public static void e(String tag, Object message) {

		if (message instanceof Exception) {
			Exception e = (Exception)message;
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			message = sw.toString();
		}

		print(Log.ERROR, tag, message);
	}

	private static void print(int logLevel, String tag, Object message) {

		if (isTest() == false) {
			return;
		}

		checkMessageLength(logLevel, tag, String.valueOf(message));
	}

	private static boolean isTest() {
		return true; // TODO
	}

	private static void checkMessageLength(int logLevel, String tag, String msg) {

		if (msg.length() < LOG_TEXT_MAX_LENGTH) {
			printMessage(logLevel, tag, msg);
			return;
		}

		printMessage(logLevel, tag, msg.substring(0, LOG_TEXT_MAX_LENGTH));

		checkMessageLength(logLevel, tag, msg.substring(LOG_TEXT_MAX_LENGTH));
	}

	private static void printMessage(int logLevel, String tag, String message) {

		switch (logLevel) {
			case Log.VERBOSE:
				Log.v(tag, message);
				break;
			case Log.DEBUG:
				Log.d(tag, message);
				break;
			case Log.INFO:
				Log.i(tag, message);
				break;
			case Log.WARN:
				Log.w(tag, message);
				break;
			case Log.ERROR:
				Log.e(tag, message);
				break;
			default:
				break;
		}
	}

}
