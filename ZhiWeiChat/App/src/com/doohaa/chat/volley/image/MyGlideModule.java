package com.doohaa.chat.volley.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

public class MyGlideModule implements GlideModule {
	private static final int MEMORY_CACHE_SIZE = 25 * 1024 * 1024;
	private static final int DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024;
	private static final int BITMAP_POOL_SIZE = 25 * 1024 * 1024;

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		// Apply options to the builder here.

		builder.setDiskCache(new InternalCacheDiskCacheFactory(context, DEFAULT_DISK_CACHE_SIZE));
		//FIXME 
		builder.setMemoryCache(new LruResourceCache(MEMORY_CACHE_SIZE));
		builder.setBitmapPool(new LruBitmapPool(BITMAP_POOL_SIZE));
	}

	@Override
	public void registerComponents(Context context, Glide glide) {
		// register ModelLoaders here.
	}
}
