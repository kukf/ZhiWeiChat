package com.doohaa.chat.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.doohaa.chat.R;
import com.doohaa.chat.utils.StringUtils;

public class OCCommonPopup extends Dialog {
	public static final float DIMMED_OPACITY = 0.7f;

	private boolean isChecked;

	public OCCommonPopup(Context context) {
		super(context);
	}

	public OCCommonPopup(Context context, int theme) {
		super(context, theme);
	}

	public boolean isChecked() {
		return this.isChecked;
	}

	public static class Builder {
		private Context context;
		private OCCommonPopupConstants constants;

		private String message;
		private View contentsView;
		private boolean doNotShowButtonVisibility = false;

		private DialogInterface.OnClickListener positiveListener;
		private DialogInterface.OnClickListener onNegativeListener;
		private DialogInterface.OnDismissListener onDismissListener;
		private OnCheckedChangeListener onCheckedChangeListener;

		public Builder(Context context, OCCommonPopupConstants constants) {
			this.context = context;
			this.constants = constants;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setContentsView(View contentsView) {
			this.contentsView = contentsView;
			return this;
		}

		public Builder setContentsView(int resource) {
			this.contentsView = LayoutInflater.from(context).inflate(resource, null);
			return this;
		}

		public Builder setDoNotShowButtonVisiblity(boolean visibility) {
			this.doNotShowButtonVisibility = visibility;
			return this;
		}

		public Builder setOnCheckedChangeListener(OnCheckedChangeListener checkedChangeListener) {
			this.onCheckedChangeListener = checkedChangeListener;
			return this;
		}

		public Builder setOnPositiveListener(DialogInterface.OnClickListener positiveListener) {
			this.positiveListener = positiveListener;
			return this;
		}

		public Builder setOnNegativeListener(DialogInterface.OnClickListener onNegativeListener) {
			this.onNegativeListener = onNegativeListener;
			return this;
		}

		public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
			this.onDismissListener = onDismissListener;
			return this;
		}

		public OCCommonPopup create() {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final OCCommonPopup dialog = new OCCommonPopup(context, R.style.TransparentProgressDialog);
			View layout = commonCustomPopLayout(inflater, dialog);

			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(true);
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			params.dimAmount = DIMMED_OPACITY;

			return dialog;
		}

		private View commonCustomPopLayout(LayoutInflater inflater, final OCCommonPopup dialog) {
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.dimAmount = 0.8f;
			dialog.getWindow().setAttributes(lp);
			dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

			View layout = inflater.inflate(R.layout.common_custom_popup, null, false);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
			dialog.addContentView(layout, params);
			TextView message = (TextView)layout.findViewById(R.id.message);
			FrameLayout contentsLayout = (FrameLayout)layout.findViewById(R.id.contents_view);
			TextView positiveBtn = (TextView)layout.findViewById(R.id.positive_button);
			TextView negativeBtn = (TextView)layout.findViewById(R.id.negative_button);

			if (contentsView == null) {
				contentsLayout.setVisibility(View.GONE);
				if (StringUtils.isNotEmpty(this.message)) {
					message.setText(this.message + StringUtils.EMPTY);
				} else if (constants.getMessage() > 0) {
					message.setText(context.getResources().getString(constants.getMessage()));
				}
			} else {
				message.setVisibility(View.GONE);
				contentsLayout.addView(contentsView);
			}

			if (constants.getPositiveBtn() > 0) {
				positiveBtn.setText(context.getResources().getString(constants.getPositiveBtn()));
			} else {
				positiveBtn.setVisibility(View.GONE);
			}

			if (positiveListener != null) {
				positiveBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						positiveListener.onClick(dialog, constants.getPositiveBtn());
					}
				});
			} else {
				positiveBtn.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}

			if (constants.getNegativeBtn() > 0) {
				negativeBtn.setText(context.getResources().getString(constants.getNegativeBtn()));
			} else {
				negativeBtn.setVisibility(View.GONE);
			}

			if (onNegativeListener != null) {
				negativeBtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onNegativeListener.onClick(dialog, constants.getNegativeBtn());
					}
				});
			} else {
				negativeBtn.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}

			if (onDismissListener != null) {
				dialog.setOnDismissListener(onDismissListener);
			}

			return layout;
		}
	}

	public interface OnCheckedChangeListener {
		void onCheckedChanged(View view, boolean isChecked);
	}
}
