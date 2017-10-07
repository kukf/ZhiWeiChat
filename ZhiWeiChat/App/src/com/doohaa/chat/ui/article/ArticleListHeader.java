package com.doohaa.chat.ui.article;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doohaa.chat.R;
import com.doohaa.chat.model.Group;
import com.lling.photopicker.utils.ImageManager;

/**
 * Created by LittleBear on 2017/9/3.
 */

public class ArticleListHeader extends LinearLayout {
    private Context context;
    private LayoutInflater layoutInflater;
    private View rootView;
    private ImageView img;
    private TextView txtGroupName;
    private TextView txtMemberCount;
    private TextView txtArticleCount;
    private TextView txtFollowState;
    private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);
    public ArticleListHeader(Context context) {
        super(context);
        initComponent(context);
    }

    public ArticleListHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public ArticleListHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArticleListHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    private void initComponent(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rootView = layoutInflater.inflate(R.layout.view_article_list_header, this, true);
        this.img = (ImageView) rootView.findViewById(R.id.img);
        this.txtGroupName = (TextView) rootView.findViewById(R.id.group_name);
        this.txtMemberCount = (TextView) rootView.findViewById(R.id.member_count);
        this.txtArticleCount = (TextView) rootView.findViewById(R.id.article_count);
        this.txtFollowState = (TextView) rootView.findViewById(R.id.follow_state);
    }

    public void setData(final Group group){
        manager.displayImage(img,"http://"+group.getImage().getUrl(), ImageManager.ShowType.URL);
        txtGroupName.setText(group.getName());
        txtMemberCount.setText(group.getMemberCount()+"");
        txtArticleCount.setText(group.getNoteCount()+"");
        if(!group.isIn()){
            txtFollowState.setText(getResources().getText(R.string.join).toString());
        }else{
            txtFollowState.setText(getResources().getText(R.string.joint).toString());
        }
    }

}
