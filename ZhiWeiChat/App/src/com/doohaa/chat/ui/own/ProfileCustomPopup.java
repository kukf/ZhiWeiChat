package com.doohaa.chat.ui.own;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doohaa.chat.R;

public class ProfileCustomPopup extends Dialog {

	public ProfileCustomPopup(Context context) {
		super(context);
	}

	public ProfileCustomPopup(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private ProfileCustomPopupListener profileCustomPopupListener;

		public Builder(Context context, ProfileCustomPopupListener profileCustomPopupListener) {
			this.context = context;
			this.profileCustomPopupListener = profileCustomPopupListener;
		}

		public ProfileCustomPopup create() {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ProfileCustomPopup dialog = new ProfileCustomPopup(context, R.style.DimmedProgressDialog);

			View layout = customPopLayout(inflater, dialog);

			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(true);

			return dialog;
		}

		private View customPopLayout(LayoutInflater inflater, final ProfileCustomPopup dialog) {
			View layout = inflater.inflate(R.layout.profile_custome_popup, null, false);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
			dialog.addContentView(layout, params);

			// camera
			layout.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					profileCustomPopupListener.onCamara();
				}
			});

			// gallery
			layout.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					profileCustomPopupListener.onGallery();
				}
			});

//			// basic image
//			layout.findViewById(R.id.basic_image).setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					dialog.dismiss();
//					profileCustomPopupListener.onBasicImage();
//				}
//			});

			return layout;
		}
	}

	public interface ProfileCustomPopupListener {
		void onCamara();

		void onGallery();

		void onBasicImage();
	}
}
