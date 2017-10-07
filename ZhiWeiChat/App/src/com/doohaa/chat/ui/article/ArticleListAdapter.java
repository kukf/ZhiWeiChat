package com.doohaa.chat.ui.article;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doohaa.chat.R;
import com.doohaa.chat.model.Article;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.ui.group.TopicFragment;
import com.doohaa.chat.utils.GlobalFunction;
import com.lling.photopicker.utils.ImageManager;

import java.util.ArrayList;

public class ArticleListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Article> datas;
    private Context context;
    private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);
    private TopicFragment frag;
    private ArticleListActivity aac;
    private Group group;

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setFrag(TopicFragment frag) {
        this.frag = frag;
    }

    public ArticleListActivity getAac() {
        return aac;
    }

    public void setAac(ArticleListActivity aac) {
        this.aac = aac;
    }

    public ArticleListAdapter(Context context, ArrayList<Article> datas) {
        this.context = context;
        this.datas = datas;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final int pos = position;
        Article article = datas.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_article_list, null);
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.img);
            holder.tvName = (TextView) convertView.findViewById(R.id.user_name);
            holder.tvContent = (TextView) convertView.findViewById(R.id.content);
            holder.tvTime = (TextView) convertView.findViewById(R.id.time);
            holder.tvReplay = (TextView) convertView.findViewById(R.id.reply_count);
            holder.tvPraise = (TextView) convertView.findViewById(R.id.praise);
            holder.tvUp = (TextView) convertView.findViewById(R.id.top);
            holder.tvDel = (TextView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemClickListener ick = new ItemClickListener();
        ick.setPos(position);
        convertView.setOnClickListener(ick);
        manager.displayImage(holder.ivAvatar,"http://"+article.getAvatar(), ImageManager.ShowType.URL);
        holder.tvName.setText(article.getOwner());
        holder.tvContent.setText(article.getMessage());
        holder.tvTime.setText(GlobalFunction.getDate(article.getCreateTime()));
        holder.tvReplay.setText(article.getReplayCount()+"");
        holder.tvPraise.setText(article.getUpCount()+"");
        holder.tvDel.setTag(pos);
        holder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer)v.getTag();
                Article article = datas.get(pos);
                if(frag != null){
                    frag.deletePost(article);
                }
                if(aac != null){
                    aac.deletePost(article);
                }
            }
        });
        holder.tvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if(article.isTop()){
            holder.tvUp.setTextColor(context.getResources().getColor(R.color.bg_red));
        }else{
            holder.tvUp.setTextColor(context.getResources().getColor(R.color.gray_normal));
        }
        return convertView;
    }


    static class ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
        TextView tvReplay;
        TextView tvPraise;
        TextView tvUp;
        TextView tvDel;
    }

    private class ItemClickListener implements View.OnClickListener {
        private int pos;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            Article article = datas.get(pos);
            Intent intent = new Intent(context, ArticleDetailActivity.class);
            intent.putExtra("item", article);
            if(group != null){
                intent.putExtra("group", group);
            }
            context.startActivity(intent);
        }
    }
}
