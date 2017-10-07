package com.doohaa.chat.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.doohaa.chat.R;
import com.doohaa.chat.api.dto.ImgPropertyDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.preferences.UserPreferences;

/**
 * Created by sunshixiong on 6/29/16.
 */
public class ImageLoaderHelper {
    private Context context;

    public ImageLoaderHelper(Context context) {
        this.context = context;
    }

    public void loadImage(String url, int errorRes, ImageView imageView) {
        Glide.with(context.getApplicationContext()).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT).error(errorRes).into(imageView);
    }

    public void loadCurrentUserImage(ImageView imageView) {
        String url = "";
        UserDto userDto = UserPreferences.getInstance(context).getUser();
        if (Validator.isNotEmpty(userDto)) {
            ImgPropertyDto imgPropertyDto = userDto.getImgProperty();
            if (Validator.isNotEmpty(imgPropertyDto)) {
                url = UIUtils.getImageUrl(imgPropertyDto);
            }
        }
        loadUserImage(url, imageView);
    }

    public void loadUserImage(String url, ImageView imageView) {
        loadImage(url, R.drawable.ease_default_avatar, imageView);
    }

    public void loadUserImage(UserDto userDto, ImageView imageView) {
        String url = "";
        if (Validator.isNotEmpty(userDto)) {
            ImgPropertyDto imgPropertyDto = userDto.getImgProperty();
            if (Validator.isNotEmpty(imgPropertyDto)) {
                url = UIUtils.getImageUrl(imgPropertyDto);
            }
        }
        loadUserImage(url, imageView);
    }
}
