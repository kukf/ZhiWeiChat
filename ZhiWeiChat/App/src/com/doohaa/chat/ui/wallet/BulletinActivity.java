package com.doohaa.chat.ui.wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.Bulletin;
import com.doohaa.chat.api.dto.BulletinDto;
import com.doohaa.chat.api.dto.ImgPropertyDto;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.utils.ImageLoaderHelper;
import com.doohaa.chat.utils.UIUtils;
import com.doohaa.chat.utils.Validator;
import com.hyphenate.easeui.widget.EaseTitleBar;

/**
 * Created by LittleBear on 2016/6/16.
 */
public class BulletinActivity extends BaseActivity implements View.OnClickListener {
    private TextView txtTitle;
    private TextView txtContent;
    private ImageView imgPic;
    private ImageLoaderHelper imageLoaderHelper;
    private EaseTitleBar titleBar;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_bulletin);
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        id = getIntent().getIntExtra("id", 0);
        txtTitle = (TextView) findViewById(R.id.bulletin_title);
        txtContent = (TextView) findViewById(R.id.content);
        imgPic = (ImageView) findViewById(R.id.img);
        imageLoaderHelper = new ImageLoaderHelper(BulletinActivity.this);
        titleBar = (EaseTitleBar) findViewById(R.id.title_bar);
    }

    private void registEvent() {
        titleBar.setLeftLayoutClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
        }
    }


    private void fetchData() {
//        showProgressDialog();
//        Response.Listener<BulletinDto> successListener = new Response.Listener<BulletinDto>() {
//            @Override
//            public void onResponse(BulletinDto response) {
//                hideProgressDialog();
//                if (Validator.isNotEmpty(response)) {
//                    Bulletin bulletin = response.getData();
//                    txtTitle.setText(bulletin.getTitle());
//                    txtContent.setText(bulletin.getBody());
//                    ImgPropertyDto imgPropertyDto = bulletin.getImgProperty();
//                    if (Validator.isNotEmpty(imgPropertyDto)) {
//                        imgPic.setVisibility(View.VISIBLE);
//                        String url = UIUtils.getImageUrl(imgPropertyDto);
//                        imageLoaderHelper.loadImage(url, R.drawable.default_load_img, imgPic);
//                    } else {
//                        imgPic.setVisibility(View.GONE);
//                    }
//                }
//            }
//        };
//
//        Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideProgressDialog();
//            }
//        };
//
//        ApiHelper.getBulletin(BulletinActivity.this, id, BulletinDto.class, successListener, errorListener);

    }
}
