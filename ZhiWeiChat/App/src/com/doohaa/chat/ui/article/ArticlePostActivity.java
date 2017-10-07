package com.doohaa.chat.ui.article;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.adapter.ImageAdapter;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.UploadImageInfoDataDto;
import com.doohaa.chat.api.dto.UploadImageInfoDto;
import com.doohaa.chat.api.dto.UploadTokenDataDto;
import com.doohaa.chat.api.dto.UploadTokenDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.db.UserDao;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.login.LoginActivity;
import com.doohaa.chat.ui.own.ProfileImageManager;
import com.doohaa.chat.utils.GlobalFunction;
import com.doohaa.chat.utils.GlobalVariables;
import com.doohaa.chat.utils.PictureUtil;
import com.doohaa.chat.utils.Validator;
import com.google.gson.Gson;
import com.lling.photopicker.PhotoPickerActivity;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ArticlePostActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipe_layout;
    private RelativeLayout rlytLeft;
    private RelativeLayout rlytRight;
    private EditText etContent;
    private GridView gv;
    private TextView tvImg;
    private ImageAdapter adapter;
    private List<String> imgs = new ArrayList<String>();// 大图
    private List<String> imgIds = new ArrayList<String>();// 图片id
    private String strImagePath;
    private Group group;
    private ProfileImageManager profileImageManager;
    private UploadManager uploadManager;
    private File fileTemp;
    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_article_post);
        group = (Group)getIntent().getSerializableExtra("item");
        initComponent();
        registEvent();
    }

    private void initComponent() {
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rlytLeft = (RelativeLayout) findViewById(R.id.left_layout);
        rlytRight = (RelativeLayout) findViewById(R.id.right_layout);
        gv = (GridView) findViewById(R.id.gridview);
        etContent = (EditText) findViewById(R.id.et_content);
        tvImg = (TextView) findViewById(R.id.tv_img);
        adapter = new ImageAdapter(this, imgs);
        gv.setAdapter(adapter);
        profileImageManager = new ProfileImageManager(this, profileImageListener);
    }

    private void registEvent() {
        rlytLeft.setOnClickListener(this);
        rlytRight.setOnClickListener(this);
        tvImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.right_layout:
                upload();
                break;
            case R.id.tv_img:
                showPhotoDialog();
                break;
            default:
                break;
        }
    }

    private void upload() {
        String content = etContent.getText().toString();
        if(TextUtils.isEmpty(content)){
            GlobalFunction.showToast(this, getText(R.string.content_is_null).toString());
            etContent.requestFocus();
            return;
        }
        if(pos == imgs.size()){
            submit();
        }else{
            String sFile = imgs.get(pos);
            fileTemp = new File(sFile);
            getUploadToken();
        }
    }

    /**
     * 拍照或者从相册选取文件
     */
    private void showPhotoDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.fx_dialog_social_main);
        TextView tvTakePhoto = (TextView) window.findViewById(R.id.tv_content1);
        tvTakePhoto.setText(getText(R.string.take_picture));
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.dismiss();
                strImagePath = GlobalFunction.getFolder(GlobalVariables.TEMP, "") + System.currentTimeMillis()
                        + ".jpg";
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File outFile = new File(strImagePath);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intent, GlobalVariables.TAKE_PICTURE);
                } else {
                    GlobalFunction.showToast(ArticlePostActivity.this,
                            getText(R.string.sdcrad_not_mount).toString());
                }
            }
        });
        TextView tvAlbum = (TextView) window.findViewById(R.id.tv_content2);
        tvAlbum.setText(getText(R.string.from_album));
        tvAlbum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.dismiss();
                int selectedMode = PhotoPickerActivity.MODE_MULTI;
                boolean showCamera = false;
                int maxNum = PhotoPickerActivity.DEFAULT_NUM - imgs.size() ;
                Intent intent = new Intent(ArticlePostActivity.this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
                intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
                intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
                startActivityForResult(intent, GlobalVariables.PICK_PHOTO);
            }
        });
    }

    private void getTwoImage(Uri uri) {
        if (uri == null) {
            GlobalFunction.showToast(this, "添加图片失败,请重试...");
            return;
        }
        String imageUrl = uri.getPath();
        String imgTemp = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        // 生成大图
        save(imageUrl, 200, "big_" + imgTemp);
        // 生成小图
        save(imageUrl, 60, imgTemp);
        Log.e("imageUrl---->>>>", imageUrl);
        Log.e("imageName_temp---->>>>", imgTemp);

        if ((new File(GlobalFunction.getFolder(GlobalVariables.TEMP, "") + imgTemp)).exists()
                && (new File(GlobalFunction.getFolder(GlobalVariables.TEMP, "") + "big_" + imgTemp)).exists()) {
            imgs.add(imageUrl);
        } else {
            GlobalFunction.showToast(this, "添加图片失败,请重试");
        }

    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    private void save(String path, int size, String saveName) {

        try {
            // File f = new File(path);

            Bitmap bm = PictureUtil.getSmallBitmap(path);
            int degree = readPictureDegree(path);

            if (degree != 0) {// 旋转照片角度
                bm = rotateBitmap(bm, degree);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            FileOutputStream fos = new FileOutputStream(
                    new File(GlobalFunction.getFolder(GlobalVariables.TEMP, ""), saveName));

            int options = 100;
            // 如果大于80kb则再次压缩,最多压缩三次
            while (baos.toByteArray().length / 1024 > size && options > 10) {
                // 清空baos
                baos.reset();
                // 这里压缩options%，把压缩后的数据存放到baos中
                bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 30;
            }

            fos.write(baos.toByteArray());
            fos.close();
            baos.close();
            // bm.compress(Bitmap.CompressFormat.JPEG, 70, fos);

            // Toast.makeText(this, "Compress OK!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case GlobalVariables.PICK_PHOTO:
                if (resultCode != RESULT_OK) {
                    break;
                }
                if (data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                    for (String path : result) {
//                        Uri uriTemp = Uri.fromFile(new File(path));
//                        getTwoImage(uriTemp);
                        compress(path);
                    }
                } else {
                    GlobalFunction.showToast(this, getText(R.string.get_picture_failed).toString());
                }
                break;
            // 如果是调用相机拍照时
            case GlobalVariables.TAKE_PICTURE:
                // Uri uri = data.getData();
                // String strImage = uri.getEncodedPath();
                if (resultCode != RESULT_OK) {
                    // GlobalFunction.showToast(this, R.string.get_picture_failed);
                    break;
                }
                File file = new File(strImagePath);
                if (file.exists()) {
                    //Uri uriTemp = Uri.fromFile(new File(strImagePath));
                    //getTwoImage(uriTemp, false);
                    compress(strImagePath);
                } else {
                    GlobalFunction.showToast(this, getText(R.string.get_picture_failed).toString());
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void compress(String path){
        Luban.with(this)
                .load(path)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(GlobalFunction.getFolder(GlobalVariables.TEMP, ""))                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        imgs.add(file.getAbsolutePath());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    private void getUploadToken() {
        showProgressDialog();
        Response.Listener<UploadTokenDto> successListener = new Response.Listener<UploadTokenDto>() {
            @Override
            public void onResponse(UploadTokenDto response) {
                if (Validator.isNotEmpty(response)) {
                    UploadTokenDataDto data = response.getData();
                    if (Validator.isNotEmpty(data)) {
                        uploadFile(data.getQnToken());
                    } else {
                        hideProgressDialog();
                        GlobalFunction.showToast(ArticlePostActivity.this,"上传图片失败，请稍后重试。");
                    }
                } else {
                    hideProgressDialog();
                    GlobalFunction.showToast(ArticlePostActivity.this,"上传图片失败，请稍后重试。");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                GlobalFunction.showToast(ArticlePostActivity.this,"上传图片失败，请稍后重试。");
            }
        };

        ApiHelper.getUploadToken(this, UploadTokenDto.class, successListener, errorListener);
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
                        hideProgressDialog();
                        GlobalFunction.showToast(ArticlePostActivity.this,"上传图片失败，请稍后重试。");
                        e.printStackTrace();
                    }
                } else {
                    hideProgressDialog();
                    GlobalFunction.showToast(ArticlePostActivity.this,"上传图片失败，请稍后重试。");
                }
            }
        };
        uploadManager.put(fileTemp, null, token, upCompletionHandler, uploadOptions);
    }

    private void uploadImageInfo(String fileName, String key) {
        Response.Listener<UploadImageInfoDto> successListener = new Response.Listener<UploadImageInfoDto>() {
            @Override
            public void onResponse(UploadImageInfoDto response) {
                hideProgressDialog();
                if (Validator.isNotEmpty(response)) {
                    UploadImageInfoDataDto data = response.getData();
                    if (Validator.isNotEmpty(data)) {
                        profileImageListener.onFileUploadSuccess(data.getImgPropertyId());
                        return;
                    }
                }
                GlobalFunction.showToast(ArticlePostActivity.this,"上传图片失败，请稍后重试。");
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                GlobalFunction.showToast(ArticlePostActivity.this,"上传图片失败，请稍后重试。");
            }
        };

        ApiHelper.upload(this, fileName, key, UploadImageInfoDto.class, successListener, errorListener);
    }

    private void submit() {
        String content = etContent.getText().toString();
        if(TextUtils.isEmpty(content)){
            GlobalFunction.showToast(this, getText(R.string.content_is_null).toString());
            etContent.requestFocus();
            return;
        }
        String url = "http://jinronghui.wang:8080/publics/note/publish.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token",token);
        params.addBodyParameter("message",content);
        params.addBodyParameter("fkGroupId",group.getId()+"");
        UserDto user = UserPreferences.getInstance(this).getUser();
        params.addBodyParameter("fkMemberId",user.getId()+"");
        StringBuffer sbf = new StringBuffer();
        int i=0;
        for(String ids : imgIds){
            if(++i == imgIds.size()){
                sbf.append(ids+"");
            }else{
                sbf.append(ids+",");
            }
        }
        params.addBodyParameter("imageIdList",sbf.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject json = new JSONObject(result);
                    String code = json.optString("code");
                    if(TextUtils.equals(code,"00")){
                        GlobalFunction.showToast(ArticlePostActivity.this,"发布成功。");
                        finish();
                    }else{
                        GlobalFunction.showToast(ArticlePostActivity.this,"发布失败，请稍后再试。");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(ArticlePostActivity.this,"发布失败，请稍后再试。");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticlePostActivity.this,"发布失败，请稍后再试。");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if(imgIds.size() == imgs.size()){
                        submit();
                    }else{
                        if (++pos>=imgs.size()) {
                            break;
                        }
                        String sFile = imgs.get(pos);
                        fileTemp = new File(sFile);
                        getUploadToken();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    ProfileImageManager.ProfileImageListener profileImageListener = new ProfileImageManager.ProfileImageListener() {
        @Override
        public void onFileUploadSuccess(String imgPropertyId) {
            imgIds.add(imgPropertyId);
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onSettingBasicImage() {

        }
    };

    @Override
    public void onRefresh() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
