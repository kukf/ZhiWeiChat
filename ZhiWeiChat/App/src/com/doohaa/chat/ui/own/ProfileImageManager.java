package com.doohaa.chat.ui.own;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.UploadImageInfoDataDto;
import com.doohaa.chat.api.dto.UploadImageInfoDto;
import com.doohaa.chat.api.dto.UploadTokenDataDto;
import com.doohaa.chat.api.dto.UploadTokenDto;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.common.OCCommonPopup;
import com.doohaa.chat.utils.ExternalStorageUtils;
import com.doohaa.chat.utils.Validator;
import com.doohaa.chat.widget.imagecrop.CropImage;
import com.doohaa.chat.widget.imagecrop.InternalStorageContentProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ProfileImageManager {

    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final int REQUEST_CODE_GALLERY = 0x1;
    private static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    private static final int REQUEST_CODE_CROP_IMAGE = 0x3;

    private File fileTemp;
    private BaseActivity activity;
    private ProfileImageListener profileImageListener;

    private OCCommonPopup commonPopup;

    private UploadManager uploadManager;

    public static ProfileImageManager getInstance(BaseActivity activity, ProfileImageListener profileImageListener) {
        return new ProfileImageManager(activity, profileImageListener);
    }

    public ProfileImageManager(BaseActivity activity, ProfileImageListener profileImageListener) {
        this.activity = activity;
        this.profileImageListener = profileImageListener;
        init();
    }

    /**
     * camera
     */
    private void camera() {
        try {
            Uri imageCaptureUri;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                imageCaptureUri = Uri.fromFile(fileTemp);
            } else {
                imageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
            activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * gallery
     */
    private void gallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
    }

    private void init() {
        // temp file
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            fileTemp = new File(ExternalStorageUtils.getCacheDirectory(activity), TEMP_PHOTO_FILE_NAME);
        } else {
            fileTemp = new File(activity.getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
        fileTemp.deleteOnExit();
    }

    private void getUploadToken() {
        activity.showProgressDialog();
        Response.Listener<UploadTokenDto> successListener = new Response.Listener<UploadTokenDto>() {
            @Override
            public void onResponse(UploadTokenDto response) {
                if (Validator.isNotEmpty(response)) {
                    UploadTokenDataDto data = response.getData();
                    if (Validator.isNotEmpty(data)) {
                        uploadFile(data.getQnToken());
                    } else {
                        activity.hideProgressDialog();
                    }
                } else {
                    activity.hideProgressDialog();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.hideProgressDialog();
            }
        };

        ApiHelper.getUploadToken(activity, UploadTokenDto.class, successListener, errorListener);
    }

    private void uploadFile(String token) {
        Log.e("xxx", "开始上传file token = " + token);


        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }
        UpProgressHandler upProgressHandler = new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {

            }
        };

        UploadOptions uploadOptions = new UploadOptions(null, null, false, upProgressHandler, null);

        UpCompletionHandler upCompletionHandler = new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    try {
                        String fileKey = response.getString("key");
                        String fileHash = response.getString("hash");
                        Log.e("xxx", "fileKey = " + fileKey + "/// key = " + key + "///// json = " + response);
                        uploadImageInfo(fileKey, fileKey);
                    } catch (JSONException e) {
                        activity.hideProgressDialog();
                        e.printStackTrace();
                    }
                } else {
                    activity.hideProgressDialog();
                }
            }
        };
        uploadManager.put(fileTemp, null, token, upCompletionHandler, uploadOptions);
    }

    private void uploadImageInfo(String fileName, String key) {
        Response.Listener<UploadImageInfoDto> successListener = new Response.Listener<UploadImageInfoDto>() {
            @Override
            public void onResponse(UploadImageInfoDto response) {
                activity.hideProgressDialog();
                if (Validator.isNotEmpty(response)) {
                    UploadImageInfoDataDto data = response.getData();
                    if (Validator.isNotEmpty(data)) {
                        profileImageListener.onFileUploadSuccess(data.getImgPropertyId());
                    }
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                activity.hideProgressDialog();
            }
        };

        ApiHelper.upload(activity, fileName, key, UploadImageInfoDto.class, successListener, errorListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_TAKE_PICTURE:
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }
                startCropImage();
                break;

            case REQUEST_CODE_GALLERY:
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }
                try {
                    InputStream inputStream = activity.getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(fileTemp);

                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    startCropImage();
                } catch (Exception e) {
                    /*Toast.makeText(activity, activity.getResources().getString(R.string.file_not_found),
                            Toast.LENGTH_SHORT).show();*/
                    e.printStackTrace();
                }

                break;

            case REQUEST_CODE_CROP_IMAGE:
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }

                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }

                uploadProfileImage(fileTemp);

                break;
        }

    }

    private String getFilePath(Uri uri) {
        ContentResolver contentResolver = activity.getContentResolver();
        String[] projection = {MediaStore.Images.Media.DATA};
        String filepath;

        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        } else {
            filepath = uri.getPath();
        }

        return filepath;
    }

    private void uploadProfileImage(File file) {
        getUploadToken();
    }

    private void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /**
     * crop picture
     */
    private void startCropImage() {
        Intent intent = new Intent(activity, CropImage.class);
        intent.putExtra(CropImage.CIRCLE_CROP, "Y");
        intent.putExtra(CropImage.IMAGE_PATH, fileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.MIN_X, 80);

        activity.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    public interface ProfileImageListener {
        void onFileUploadSuccess(String imgPropertyId);

        void onSettingBasicImage();
    }

    public void showPopup() {
        ProfileCustomPopup.Builder popup = new ProfileCustomPopup.Builder(activity, popupListener);
        popup.create().show();
    }

    private ProfileCustomPopup.ProfileCustomPopupListener popupListener = new ProfileCustomPopup.ProfileCustomPopupListener() {
        @Override
        public void onCamara() {
            camera();
        }

        @Override
        public void onGallery() {
            gallery();
        }

        @Override
        public void onBasicImage() {
        }
    };
}
