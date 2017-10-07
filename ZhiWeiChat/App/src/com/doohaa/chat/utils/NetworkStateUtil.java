/*
 * NetworkState.java $version 2012. 09. 03
 *
 * Copyright 2012 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.doohaa.chat.utils;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

public class NetworkStateUtil {

	private static final int[] DATA_NETWORK_TYPES = new int[]{ConnectivityManager.TYPE_MOBILE,
			ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_WIMAX};
	private static final String WIFI_TYPE_NAME = "WIFI";

	public static boolean isNetworkAvailable(Context context) {
		if (context == null) {
			return false;
		}

		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		} else {
			return networkInfo.isAvailable();
		}
	}

	public static boolean isAirplainMode(Context context) {
		boolean isEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
		return (isEnabled);
	}

	// 사용자에 의해서 Disabled 된 경우
	public static boolean isNetworkDisabled(Context context) {
		return (isAirplainMode(context) == false && isWifiConnected(context) == false);
	}

	public static boolean isDataConnected(Context context) {
		boolean connected = false;

		try {
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();

			if (info != null) {
				connected = manager.getActiveNetworkInfo().isConnected();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return connected;
	}

	public static boolean is3GConnected(Context context) {
		NetworkInfo mobile = null;
		try {
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (manager != null) {
				mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobile != null && mobile.isConnected()) {
					return (true);
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return (false);
	}

	public static boolean isWifiConnected(Context context) {
		try {
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (manager != null) {
				NetworkInfo wifiNetInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo wimaxNetInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
				if (wifiNetInfo == null && wimaxNetInfo == null) {
					return false;
				}

				if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
					return true;
				}

				if (wimaxNetInfo != null && wimaxNetInfo.isConnected()) {
					return true;
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		return false;
	}

	/**
	 * 데이터 네트워크 상태정보를 문자열로 조회한다.
	 */
	public static String getDataNetworkStatus(Context context) {
		StringBuffer sb = new StringBuffer();
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity == null) {
			} else {
				for (int type : DATA_NETWORK_TYPES) {
					NetworkInfo net = connectivity.getNetworkInfo(type);

					if (net != null) {

						sb.append("# ").append(net.getTypeName());
						if (StringUtils.isNotEmpty(net.getSubtypeName())) {
							sb.append("[").append(net.getSubtypeName()).append("]");
						}

						if (WIFI_TYPE_NAME.equals(net.getTypeName()) && net.isAvailable()) {
							sb.append("[").append(getWifiApName(context)).append("]");
						}

						sb.append(net.getState()).append("/").append(net.getDetailedState());
						sb.append("/available : ").append(net.isAvailable());

						sb.append(" ");
					}
				}
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}

	public static String getWifiApName(Context context) {
		WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiMan != null) {
			WifiInfo wifiInf = wifiMan.getConnectionInfo();
			return wifiInf.getSSID();
		}
		return "";
	}

	/**
	 * Returns MAC address of the given interface name.
	 *
	 * @param interfaceName eth0, wlan0 or NULL=use first interface
	 * @return mac address or empty string
	 */
	public static String getMACAddress(String interfaceName) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName))
						continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac == null)
					return "";
				StringBuilder buf = new StringBuilder();
				for (int idx = 0; idx < mac.length; idx++)
					buf.append(String.format("%02X:", mac[idx]));
				if (buf.length() > 0)
					buf.deleteCharAt(buf.length() - 1);
				return buf.toString();
			}
		} catch (Exception ex) {
		} // for now eat exceptions
		return null;
	}
}
