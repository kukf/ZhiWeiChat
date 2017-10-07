package com.doohaa.chat.volley;

import java.io.File;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.NoCache;

public class MyVolley {
	private static final int DEFAULT_CACHE_SIZE = 10 * 1024 * 1024;
	private static final long DEFAULT_MAX_AGE = 60;

	private static RequestQueue requestQueue;
	private static RequestQueue fileUploadQueue;

	public static void init(Context context) {
		if (context == null) {
			throw new NullPointerException("context must not be null.");
		}

		requestQueue = createRequestQueue(context);

		fileUploadQueue = createFileUploadRequestQueue(context);

		requestQueue.start();
		fileUploadQueue.start();
	}

	private static RequestQueue createFileUploadRequestQueue(Context context) {
		Cache diskCache = getDefaultDiskCache(context);
		//always build version over 11
		HttpStack httpStack = new HttpUrlStack();

		return new RequestQueue(diskCache, new BasicNetwork(httpStack));
	}

	private static RequestQueue createRequestQueue(Context context) {
		Cache diskCache = getDefaultDiskCache(context);
		HttpStack httpStack = new HttpUrlStack();

		return new RequestQueue(diskCache, new BasicNetwork(httpStack));
	}

	public static RequestQueue getRequestQueue() {
		if (requestQueue == null) {
			throw new IllegalStateException("RequestQueue is not initialized.");
		}
		return requestQueue;
	}

	public static RequestQueue getFileUploadQueue() {
		if (fileUploadQueue == null) {
			throw new IllegalStateException("fileUploadQueue is not initialized.");
		}
		return fileUploadQueue;
	}

	private static Cache getDefaultDiskCache(Context context) {
		File cacheDir = getCacheDir(context);
		if (cacheDir == null) {
			return new NoCache();
		}

		return new DiskBasedCache(cacheDir);
	}

	private static File getCacheDir(Context context) {
		File file = new File(context.getCacheDir().getPath()
			+ "/cache");
		return file;
	}

}
