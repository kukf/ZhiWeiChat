package com.doohaa.chat.ui.common;

import android.content.Context;
import android.content.Intent;

import com.doohaa.chat.ui.MainActivity;

/**
 * Created by LittleBear on 2016/5/18.
 */
public class JumpUtils {
    public static void goToMainPage(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
