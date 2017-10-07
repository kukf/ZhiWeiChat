package com.lling.photopicker.utils;
import com.lling.photopicker.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.widget.ImageView;

public class ImageManager {
	public enum ShowType {
		FILE, URL
	}

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public ImageManager() {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_photo_loading) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_photo_loading) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_photo_loading) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(false)
				// 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(0))// 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象
	}

	public ImageManager(int resourceId, int round) {
		options = new DisplayImageOptions.Builder().showStubImage(resourceId) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(resourceId) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(resourceId) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(false) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(0))// 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象
	}

	public ImageManager(int resourceId) {
		options = new DisplayImageOptions.Builder().showStubImage(resourceId) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(resourceId) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(resourceId) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(false) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(0))// 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象
	}

	public void displayImage(ImageView iv, String url, ShowType type) {
		if (type == ShowType.FILE) {
			imageLoader.displayImage("file://" + url, iv, options,
					animateFirstListener);
		} else if (type == ShowType.URL) {
			imageLoader.displayImage(url, iv, options, animateFirstListener);
		}
	}

	public void clearCache() {
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
	}

	public void displayImage(ImageView iv, int resourceId) {
		imageLoader.displayImage("drawable://" + resourceId, iv, options,
				animateFirstListener);
	}

}
