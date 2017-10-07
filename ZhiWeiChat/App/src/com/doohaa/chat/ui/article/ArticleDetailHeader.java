package com.doohaa.chat.ui.article;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doohaa.chat.R;
import com.doohaa.chat.adapter.ImageAdapter2;
import com.doohaa.chat.model.Article;
import com.doohaa.chat.model.Image;
import com.doohaa.chat.utils.GlobalFunction;
import com.lling.photopicker.utils.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailHeader extends LinearLayout {
    private Context context;
    private LayoutInflater layoutInflater;
    private View rootView;
    private GridView gridview;
    private TextView praise;
    private TextView reply;
    private TextView time;
    private TextView content;
    private TextView del;
    private TextView up;
    private TextView name;
    private ImageView avatar;
    private ImageAdapter2 adapter;
    private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);
    public ArticleDetailHeader(Context context) {
        super(context);
        initComponent(context);
    }

    public ArticleDetailHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context);
    }

    public ArticleDetailHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArticleDetailHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context);
    }

    private void initComponent(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.rootView = layoutInflater.inflate(R.layout.view_article_detail_header, this, true);
        gridview = (GridView)rootView.findViewById(R.id.gridview);
        avatar = (ImageView)rootView.findViewById(R.id.img);
        name = (TextView)rootView.findViewById(R.id.user_name);
        del = (TextView)rootView.findViewById(R.id.delete);
        up = (TextView)rootView.findViewById(R.id.top);
        time = (TextView)rootView.findViewById(R.id.time);
        content = (TextView)rootView.findViewById(R.id.content);
        reply = (TextView)rootView.findViewById(R.id.reply_count);
        praise = (TextView)rootView.findViewById(R.id.praise);
    }

    public void setData(Article article){
        List<String> items = new ArrayList<>();
        List<Image> images = article.getImageList();
        if(images == null || images.size() == 0){
            gridview.setVisibility(View.GONE);
        }else{
            gridview.setVisibility(View.VISIBLE);
            for(Image img : images){
                items.add(img.getUrl());
            }
            adapter = new ImageAdapter2(context,items);
            gridview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        manager.displayImage(avatar,"http://"+article.getAvatar(), ImageManager.ShowType.URL);
        name.setText(article.getMemberName());
        content.setText(article.getMessage());
        String sTime = GlobalFunction.getDate(article.getCreateTime());
        time.setText(sTime);
        reply.setText(article.getReplayCount()+"");
        praise.setText(article.getUpCount()+"");
        Drawable dw = null;
        if(article.isUp()){
            dw = getResources().getDrawable(R.drawable.praised);
        }else{
            dw = getResources().getDrawable(R.drawable.no_praise);
        }
        dw.setBounds(0, 0, dw.getMinimumWidth(), dw.getMinimumHeight());
        praise.setCompoundDrawables(dw,null,null,null);
        if(article.isOwner()){
            del.setVisibility(View.VISIBLE);
            up.setVisibility(View.GONE);
        }else{
            del.setVisibility(View.GONE);
            up.setVisibility(View.VISIBLE);
        }
    }

    public void setPraise(OnClickListener clk1) {
        praise.setOnClickListener(clk1);
    }

    public void setUp(OnClickListener clk1) {
        up.setOnClickListener(clk1);
    }

    public void setDel(OnClickListener clk1) {
        del.setOnClickListener(clk1);
    }

    public void setPraiseCount(boolean action){
        int num = Integer.parseInt(praise.getText().toString());
        Drawable dw = null;
        if(!action){
            num--;
            dw = getResources().getDrawable(R.drawable.no_praise);
        }else{
            num++;
            dw = getResources().getDrawable(R.drawable.praised);
        }
        dw.setBounds(0, 0, dw.getMinimumWidth(), dw.getMinimumHeight());
        praise.setCompoundDrawables(dw,null,null,null);
        praise.setText(num+"");
    }

}
