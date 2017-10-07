package com.doohaa.chat.ui.common;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.doohaa.chat.R;

public class ProgressDialogFragment extends DialogFragment {

	public static final String TAG = "ProgressDialogFragment";
	private static final String KEY_TAG = "key_tag";

	private Dialog dialog = null;

	private boolean touchable;

	private boolean cancelable;

	private OnCancelListener cancelListener;
	private String tag;

	/**
	 * @return
	 */
	public static ProgressDialogFragment newInstance(String tag) {
		ProgressDialogFragment dialog = new ProgressDialogFragment();
		Bundle args = new Bundle();
		args.putString(KEY_TAG, tag);
		dialog.setArguments(args);

		return dialog;
	}

	/**
	 * @param savedInstanceState
	 * @return
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (getArguments() != null) {
			tag = getArguments().getString(KEY_TAG);
		}

		if (touchable) {
			dialog = new Dialog(getActivity(), R.style.TransparentProgressDialog);
			dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		} else {
			dialog = new Dialog(getActivity(), R.style.DimmedProgressDialog);
		}

		dialog.addContentView(new ProgressBar(getActivity()), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(false);

		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (cancelListener != null) {
						cancelListener.onCancel(dialog, tag);
					}
					return true;
				}

				return false;
			}
		});

		return dialog;
	}

	/**
	 * @param touchable
	 */
	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}

	/**
	 * @param cancelable
	 * @see android.support.v4.app.DialogFragment#setCancelable(boolean)
	 */
	@Override
	public void setCancelable(boolean cancelable) {
		super.setCancelable(cancelable);

		this.cancelable = cancelable;
	}

	/**
	 * @param cancelListener
	 */
	public void setOnCancelListener(OnCancelListener cancelListener) {
		this.cancelListener = cancelListener;
	}

	/**
	 * @see android.support.v4.app.DialogFragment#dismiss()
	 */
	@Override
	public void dismiss() {
		super.dismiss();

		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public interface OnCancelListener {
		void onCancel(DialogInterface dialog, String tag);
	}
}
