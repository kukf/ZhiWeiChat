package com.doohaa.chat.utils;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doohaa.chat.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalFunction {
	public static final String REGEX_IS_MOBILE = "(?is)(^1[3|4|5|7|8][0-9]\\d{4,8}$)";
	public static void showToast(Context context, int strId) {
		if (context == null) {
			return;
		}
		String content = context.getText(strId).toString();
		showToast(context, content);
	}

	public static String getDate(long time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		String t = format.format(new Date(time));
		return t;
	}

	public static String getFolder(int messageType, String toId) {
		String strFolder = null;
		switch (messageType) {
			case GlobalVariables.MSG_IMAGE:
				strFolder = GlobalVariables.ROOT_PATH + toId + GlobalVariables.IMAGE_FLODER;
				break;
			case GlobalVariables.MSG_AUDIO:
				strFolder = GlobalVariables.ROOT_PATH + toId + GlobalVariables.AUDIO_FOLDER;
				break;
			case GlobalVariables.AVATAR_USER:
				strFolder = GlobalVariables.AVATAR_FOLDER;
				break;
			case GlobalVariables.APK:
				strFolder = GlobalVariables.APK_PATH;
				break;
			case GlobalVariables.TEMP:
				strFolder = GlobalVariables.TEMP_PATH;
				break;
			case GlobalVariables.HTML:
				strFolder = GlobalVariables.HTML_FOLDER;
				break;
			default:
				break;
		}
		File f = new File(strFolder);
		try {
			if (!f.exists()) {
				boolean s = f.mkdirs();
				System.out.println("fdb");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strFolder;
	}

	public static void showToast(Context context, String content) {
		try {
			LayoutInflater inflater = LayoutInflater.from(context);
			View layout = inflater.inflate(R.layout.toast, null);
			TextView text = (TextView) layout.findViewById(R.id.toast_text);
			text.setText(content);
			layout.getBackground().setAlpha(125);// 0~255透明度值
			Toast toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, -70);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(layout);
			toast.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	
	/**
	 * 验证手机号
	 * 
	 * @param mobileNumber
	 * @return
	 */
	public static boolean veriyMobile(String mobileNumber) {
		Pattern p = null;
		Matcher m = null;

		p = Pattern.compile(REGEX_IS_MOBILE);
		m = p.matcher(mobileNumber);

		return m.matches() && (mobileNumber.length() == 11);
	}
	
	/**
	 * 判断是否包含非法字符
	 * 
	 * @param content
	 * @return true 包含非法字符
	 */
	public static boolean isContainsIllegalCharacter(String content) {
		char[] charArray = content.toCharArray();
		for (char c : charArray) {
			if (c == '_' || (c >= 48 && c <= 57) || (c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')) {
				continue;
			}
			return true;
		}

		return false;
	}

	public static boolean containsIllegalCharacter(String content) {
		return !content.matches("[a-zA-Z0-9_\u4e00-\u9fa5]*");
	}
	
}
