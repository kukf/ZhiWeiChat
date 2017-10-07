package com.doohaa.chat.ui.article;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

class ArticleDetailAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Note> datas;
    private Context context;
    private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);
    public ArticleDetailAdapter(Context context, ArrayList<Note> datas) {
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
        Note note = datas.get(pos);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_article_detail, null);
            holder.tvMore = (TextView)convertView.findViewById(R.id.more);
            holder.floor = (TextView)convertView.findViewById(R.id.floor);
            holder.name = (TextView)convertView.findViewById(R.id.user_name);
            holder.img = (ImageView)convertView.findViewById(R.id.img);
            holder.del = (TextView)convertView.findViewById(R.id.delete);
            holder.answer = (TextView)convertView.findViewById(R.id.answer);
            holder.content = (TextView)convertView.findViewById(R.id.content);
            holder.time = (TextView)convertView.findViewById(R.id.time);
            holder.llItem1 = (LinearLayout) convertView.findViewById(R.id.ll_item1);
            holder.llItem2 = (LinearLayout)convertView.findViewById(R.id.ll_item2);
            holder.name1 = (TextView)convertView.findViewById(R.id.name1);
            holder.content1 = (TextView)convertView.findViewById(R.id.content1);
            holder.del1 = (TextView)convertView.findViewById(R.id.del1);
            holder.name2 = (TextView)convertView.findViewById(R.id.name2);
            holder.content2 = (TextView)convertView.findViewById(R.id.content2);
            holder.del2 = (TextView)convertView.findViewById(R.id.del2);
            holder.line = (View)convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvMore.setOnClickListener(new MoreClickEvent());
        holder.content.setText(note.getMessage());
        holder.floor .setText(pos+context.getResources().getText(R.string.floor).toString());
        manager.displayImage(holder.img,note.getImage().getUrl(), ImageManager.ShowType.URL);
        holder.name.setText("");
        holder.time.setText(GlobalFunction.getDate(note.getCreateTime()));
        List<NoteMessage> notes = note.getNotes();
        holder.tvMore.setVisibility(View.GONE);
        if(notes == null || notes.size() == 0){
            holder.line.setVisibility(View.GONE);
            holder.llItem1.setVisibility(View.GONE);
            holder.llItem2.setVisibility(View.GONE);
        }else{
            if(notes.size() >= 1){
                holder.llItem1.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
                NoteMessage msg = notes.get(0);
                holder.name1.setText(msg.getMemberName()+":");
                holder.content1.setText(msg.getMessage());
//                holder.del1.setText("ɾ��");
            }
            if(notes.size() >= 2){
                holder.llItem2.setVisibility(View.VISIBLE);
                NoteMessage msg = notes.get(1);
                holder.name2.setText(msg.getMemberName()+":");
                holder.content2.setText(msg.getMessage());
//                holder.del2.setText("ɾ��");
            }
            if(notes.size() > 2){
                holder.tvMore.setVisibility(View.VISIBLE);
            }
            holder.answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetailActivity dc = (ArticleDetailActivity)context;
                    Note nm = datas.get(pos);
                    dc.answer(nm);
                }
            });
        }
        return convertView;
    }


    static class ViewHolder {
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
    }

    private class MoreClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ReplyDetailActivity.class);
            context.startActivity(intent);
        }
    }
}
