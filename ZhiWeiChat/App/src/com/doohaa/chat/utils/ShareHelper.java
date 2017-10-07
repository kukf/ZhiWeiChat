package com.doohaa.chat.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doohaa.chat.ui.common.OCToast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sunshixiong on 6/7/16.
 */
public class ShareHelper {
    private static final String TAG = "ShareHelper";
    private static final String strPath = "/ZhiWeiApp";

    private Context context;

    public ShareHelper(Context context) {
        this.context = context;
    }

    public void share(View view) {

        if (!checkFolder()) {
            return;
        }

        try {
            File file = saveBitmapFile(shot(view));
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.SHARE_SUB_TITLE);
            shareIntent.putExtra(Intent.EXTRA_TITLE, Constants.SHARE_TITLE);
            shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.SHARE_TEXT);
            if (file != null) {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            }
            shareIntent.setType("text/plain");

            context.startActivity(Intent.createChooser(shareIntent, Constants.SHARE_BAR));
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Fail to share ==> ActivityNotFoundException");
        } catch (IOException e) {
            Log.e(TAG, "Fail to share ==> IOException");
        }
    }

    private File saveBitmapFile(Bitmap bitmap) throws IOException {
        String fileName = String.valueOf(System
                .currentTimeMillis()) + ".jpg";
        File file = new File(getFolderDirectory() + fileName);//将要保存图片的路径
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return file;
    }

    private Bitmap shot(View view) {
        Bitmap bitmap;
        view.setDrawingCacheEnabled(true);
        bitmap = view.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 检测文件夹是否存在
     *
     * @return
     */
    private boolean checkFolder() {
        boolean isOK = false;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            long sdFreeSize = getSDFreeSize();
            if (sdFreeSize > 512) {
                File file = new File(getFolderDirectory());
                if (!file.exists()) {
                    if (file.mkdirs()) {
                        isOK = true;
                    } else {
                        isOK = false;
                    }
                } else {
                    isOK = true;
                }
            } else {
                OCToast.makeToast(context, "SD卡剩余空间不足512M！无法使用此功能!", Toast.LENGTH_SHORT).show();
                isOK = false;
            }
        } else {
            OCToast.makeToast(context, "SD卡不可用！无法使用此功能!", Toast.LENGTH_SHORT).show();
            isOK = false;
        }
        return isOK;
    }

    public String getFolderDirectory() {
        return Environment.getExternalStorageDirectory().getPath() + strPath
                + File.separator;
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return
     */
    private long getSDFreeSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize();
        long freeBlocks = sf.getAvailableBlocks();
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 获取文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean fileExists(String fileName) {
        boolean isOK = false;
        if (checkFolder()) {
            String filePath = getFolderDirectory() + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                isOK = true;
            } else {
                isOK = false;
            }
        } else {
            isOK = false;
        }
        return isOK;
    }
}
