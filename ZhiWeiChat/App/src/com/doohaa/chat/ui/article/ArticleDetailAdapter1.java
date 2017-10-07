package com.doohaa.chat.ui.article;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.doohaa.chat.R;
import com.doohaa.chat.model.Note;
import com.doohaa.chat.model.NoteMessage;
import com.doohaa.chat.utils.GlobalFunction;
import com.lling.photopicker.utils.ImageManager;

import java.util.ArrayList;
import java.util.List;

class ArticleDetailAdapter1 extends RecyclerView.Adapter<ArticleDetailAdapter1.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<Note> datas;
    private Context context;
    private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);
    public ArticleDetailAdapter1(Context context, ArrayList<Note> datas) {
        this.context = context;
        this.datas = datas;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_article_detail, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int pos = position;
        Note note = datas.get(pos);
        holder.content.setText(note.getMessage());
        holder.floor .setText((pos+1)+context.getResources().getText(R.string.floor).toString());
        manager.displayImage(holder.img,note.getImage().getUrl(), ImageManager.ShowType.URL);
        holder.name.setText(note.getOwner());
        holder.time.setText(GlobalFunction.getDate(note.getCreateTime()));
        List<NoteMessage> notes = note.getNotes();
        holder.tvMore.setVisibility(View.GONE);
        holder.answer.setTag(pos);
        holder.del.setTag(pos);
        holder.del1.setTag(pos);
        holder.del2.setTag(pos);
        holder.tvMore.setTag(pos);
        if(notes == null || notes.size() == 0){
            holder.line.setVisibility(View.GONE);
            holder.llItem1.setVisibility(View.GONE);
            holder.llItem2.setVisibility(View.GONE);
        }else {
            if (notes.size() >= 1) {
                holder.llItem1.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
                NoteMessage msg = notes.get(0);
                holder.name1.setText(msg.getMemberName() + ":");
                holder.content1.setText(msg.getMessage());
            }
            if (notes.size() >= 2) {
                holder.llItem2.setVisibility(View.VISIBLE);
                NoteMessage msg = notes.get(1);
                holder.name2.setText(msg.getMemberName() + ":");
                holder.content2.setText(msg.getMessage());
            }
            if (notes.size() > 2) {
                holder.tvMore.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvMore;
        TextView floor;
        TextView name;
        TextView answer;
        TextView del;
        TextView content;
        TextView time;
        LinearLayout llItem1;
        TextView name1;
        TextView content1;
        TextView del1;
        LinearLayout llItem2;
        TextView name2;
        TextView content2;
        TextView del2;
        View line;

        public MyViewHolder(View convertView) {
            super(convertView);
            tvMore = (TextView)convertView.findViewById(R.id.more);
            floor = (TextView)convertView.findViewById(R.id.floor);
            name = (TextView)convertView.findViewById(R.id.user_name);
            img = (ImageView)convertView.findViewById(R.id.img);
            del = (TextView)convertView.findViewById(R.id.delete);
            answer = (TextView)convertView.findViewById(R.id.answer);
            content = (TextView)convertView.findViewById(R.id.content);
            time = (TextView)convertView.findViewById(R.id.time);
            llItem1 = (LinearLayout) convertView.findViewById(R.id.ll_item1);
            llItem2 = (LinearLayout)convertView.findViewById(R.id.ll_item2);
            name1 = (TextView)convertView.findViewById(R.id.name1);
            content1 = (TextView)convertView.findViewById(R.id.content1);
            del1 = (TextView)convertView.findViewById(R.id.del1);
            name2 = (TextView)convertView.findViewById(R.id.name2);
            content2 = (TextView)convertView.findViewById(R.id.content2);
            del2 = (TextView)convertView.findViewById(R.id.del2);
            line = (View)convertView.findViewById(R.id.line);
            tvMore.setOnClickListener(new MoreClickEvent());
            answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer)v.getTag();
                    ArticleDetailActivity dc = (ArticleDetailActivity) context;
                    Note nm = datas.get(pos);
                    dc.answer(nm);
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer)v.getTag();
                    ArticleDetailActivity dc = (ArticleDetailActivity) context;
                    Note nm = datas.get(pos);
                    dc.deleteReplay(nm);
                }
            });
            del1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer)v.getTag();
                    ArticleDetailActivity dc = (ArticleDetailActivity) context;
                    Note nm = datas.get(pos);
                    List<NoteMessage> nms = nm.getNotes();
                    dc.deleteComment(nm,nms.get(0).getId());
                }
            });
            del2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (Integer)v.getTag();
                    ArticleDetailActivity dc = (ArticleDetailActivity) context;
                    Note nm = datas.get(pos);
                    List<NoteMessage> nms = nm.getNotes();
                    dc.deleteComment(nm,nms.get(1).getId());
                }
            });
        }
    }

    private class MoreClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ReplyDetailActivity.class);
            int pos = (Integer)v.getTag();
            Note nm = datas.get(pos);
            intent.putExtra("item",nm);
            context.startActivity(intent);
        }
    }
}
