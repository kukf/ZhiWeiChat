package com.doohaa.chat.ui.common;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doohaa.chat.R;
import com.doohaa.chat.utils.StringUtils;

public class OCToast {
	private static final String REGEX = "\\{(.+)\\}";

	public static Toast makeToast(Context context, int resId, int duration) {
		String text = context.getResources().getString(resId);

		StringBuilder result = new StringBuilder();

		Matcher matcher = Pattern.compile(REGEX).matcher(text);

		List<String> highlightTexts = new ArrayList();

		int start;
		int end = 0;
		while (matcher.find()) {
			String highlight = matcher.group(1);

			start = matcher.start();

			result.append(text.substring(end, start)).append(highlight);

			highlightTexts.add(highlight);

			end = matcher.end();
		}

		result.append(text.substring(end, text.length()));

		return makeToast(context, result, duration, highlightTexts.toArray(new String[highlightTexts.size()]));
	}

	public static Toast makeToast(Context context, CharSequence text, int duration, CharSequence... highlightTexts) {

		if (StringUtils.isEmpty(text) || highlightTexts == null || highlightTexts.length <= 0) {
			return makeToast(context, text, duration);
		}

		SpannableString spannableString = new SpannableString(text);

		for (CharSequence highlightText : highlightTexts) {
			int startIndex = text.toString().indexOf(highlightText.toString());
			int endIndex = startIndex + highlightText.length();

			int color = Color.parseColor("#f26161");
			spannableString.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		return makeToast(context, spannableString, duration);
	}

	private static Toast makeToast(Context context, CharSequence text, int duration) {
		Toast toast = ToastManager.getInstance().getToast();

		if (toast == null) {
			toast = new Toast(context);
		}

		View v;
		if (toast.getView() == null) {
			LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflate.inflate(R.layout.custom_toast_layout, null);

			toast.setDuration(duration);
			toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, context.getResources().getDimensionPixelSize(R.dimen.custom_toast_bottom_margin));

			toast.setView(v);
		} else {
			v = toast.getView();
		}

		TextView tv = (TextView)v.findViewById(R.id.message);
		tv.setText(text);

		return ToastManager.getInstance().manage(toast);
	}

	public static class ToastManager {
		WeakReference<Toast> toastRef;

		private static ToastManager instance;

		public static ToastManager getInstance() {
			if (instance == null) {
				synchronized (ToastManager.class) {
					if (instance == null) {
						instance = new ToastManager();
					}
				}
			}
			return instance;
		}

		public Toast getToast() {
			if (toastRef != null && toastRef.get() != null) {
				return toastRef.get();
			}
			return null;
		}

		public void cancel() {
			if (toastRef != null && toastRef.get() != null) {
				toastRef.get().cancel();
			}
		}

		public Toast manage(Toast toast) {
			toastRef = new WeakReference(toast);

			return toast;

		}
	}
}
