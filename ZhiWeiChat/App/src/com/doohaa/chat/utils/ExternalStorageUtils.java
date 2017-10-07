/*
 * StorageUtils.java
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.doohaa.chat.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class ExternalStorageUtils {
	private static final String TAG = "ExternalStorageUtils";

	public static boolean isAvailable() {
		boolean available = false;
		boolean writeable = false;

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			available = writeable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			available = true;
			writeable = false;
		} else {
			available = writeable = false;
		}

		return (available == true && writeable == true);
	}

	public static long getFreeSpace() {
		if (isAvailable() == false) {
			return 0;
		}

		File directory = getDirectory();
		if (directory == null) {
			return 0;
		}

		StatFs statFs = new StatFs(directory.getAbsolutePath());
		return (long)statFs.getBlockSize() * statFs.getFreeBlocks();
	}

	public static File getDirectory() {
		if (isAvailable() == false) {
			return null;
		}

		// android 4.2 sdcard 버그
		if (Build.VERSION.SDK_INT == 17) {
			return new File("/sdcard");
		}

		return Environment.getExternalStorageDirectory();
	}

	public static File getPublicPictureDirectory() {
		if (isAvailable() == false) {
			return null;
		}

		if (UIUtils.hasFroyo()) {
			return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		} else {
			File directory = getDirectory();
			if (directory == null) {
				return null;
			}
			return new File(directory.getAbsolutePath() + "/Pictures");
		}
	}

	public static File getPublicMovieDirectory() {
		if (isAvailable() == false) {
			return null;
		}

		if (UIUtils.hasFroyo()) {
			return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
		} else {
			File directory = getDirectory();
			if (directory == null) {
				return null;
			}
			return new File(directory.getAbsolutePath() + "/Movies");
		}
	}

	public static File getPublicMusicDirectory() {
		if (isAvailable() == false) {
			return null;
		}

		if (UIUtils.hasFroyo()) {
			return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		} else {
			File directory = getDirectory();
			if (directory == null) {
				return null;
			}
			return new File(directory.getAbsolutePath() + "/Music");
		}
	}

	public static File getPictureDirectory(Context context) {
		if (isAvailable() == false || context == null) {
			return null;
		}

		if (UIUtils.hasFroyo()) {
			return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		} else {
			File directory = getDirectory();
			if (directory == null) {
				return null;
			}
			return new File(directory.getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/Pictures");
		}
	}

	public static File getMovieDirectory(Context context) {
		if (isAvailable() == false || context == null) {
			return null;
		}

		if (UIUtils.hasFroyo()) {
			return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
		} else {
			File directory = getDirectory();
			if (directory == null) {
				return null;
			}
			return new File(directory.getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/Movies");
		}
	}

	public static File getMusicDirectory(Context context) {
		if (isAvailable() == false || context == null) {
			return null;
		}

		if (UIUtils.hasFroyo()) {
			return context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
		} else {
			File directory = getDirectory();
			if (directory == null) {
				return null;
			}
			return new File(directory.getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/Music");
		}
	}

	public static File getCacheDirectory(Context context) {
		if (isAvailable() == false || context == null) {
			return null;
		}

		if (UIUtils.hasFroyo()) {
			return context.getExternalCacheDir();
		} else {
			File directory = getDirectory();
			if (directory == null) {
				return null;
			}
			File path = new File(directory.getAbsolutePath() + "/Android/data/" + context.getPackageName() + "/cache");
			createNoMediaFile(path);
			return path;
		}
	}

	public static void createNoMediaFile(File path) {
		File nomedia = new File(path, ".nomedia");
		if (!nomedia.exists()) {
			try {
				nomedia.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void runMediaScanner(Context context, File file) {
		MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
			public void onScanCompleted(String path, Uri uri) {
				Log.i(TAG, "Scanned " + path + uri);
			}
		});
	}
}
