package com.doohaa.chat.ui.article;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.doohaa.chat.R;

public class ReplyDetailHeader extends LinearLayout {
    private Context context;
    private LayoutInflater layoutInflater;
    private View rootView;

    public ReplyDetailHeader(Context context) {
        super(context);
        initComponent(context);
    }

    public ReplyDetailHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public ReplyDetailHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReplyDetailHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    private void initComponent(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rootView = layoutInflater.inflate(R.layout.view_reply_detail_header, this, true);
    }

}
