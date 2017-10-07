package com.doohaa.chat.volley.image;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class ScaleErrorImageViewTarget extends GlideDrawableImageViewTarget {
	ImageView.ScaleType mErrorScaleType;

	public ScaleErrorImageViewTarget(ImageView view, ImageView.ScaleType errorScaleType) {
		super(view);
		mErrorScaleType = errorScaleType;
	}

	@Override
	public void onLoadFailed(Exception e, Drawable errorDrawable) {
		ImageView imageView = getView();
		imageView.setScaleType(mErrorScaleType);
		super.onLoadFailed(e, errorDrawable);
	}

	@NonNull
	public static ScaleErrorImageViewTarget errorCenterTarget(ImageView imageView) {
		return errorScaleTypeTarget(imageView, ImageView.ScaleType.CENTER);
	}

	@NonNull
	public static ScaleErrorImageViewTarget errorScaleTypeTarget(ImageView imageView, ImageView.ScaleType scaleType) {
		return new ScaleErrorImageViewTarget(imageView, scaleType);
	}
}
