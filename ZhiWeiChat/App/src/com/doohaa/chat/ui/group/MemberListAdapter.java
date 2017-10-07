package com.doohaa.chat.ui.group;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doohaa.chat.R;
import com.doohaa.chat.model.GroupMember2;
import com.lling.photopicker.utils.ImageManager;

import java.util.ArrayList;

class MemberListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<GroupMember2> datas;
    private Context context;
    private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);
    public MemberListAdapter(Context context, ArrayList<GroupMember2> datas) {
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
        GroupMember2 m = datas.get(pos);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_member_list, null);
            holder.avatar = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            holder.tvDisAgree = (TextView) convertView.findViewById(R.id.shotout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        manager.displayImage(holder.avatar,"http://"+m.getUrl(), ImageManager.ShowType.URL);
        holder.name.setText(m.getName());
        holder.tvDisAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberListActivity mla = (MemberListActivity)context;
                mla.kick(pos);
            }
        });
//        holder.btnDisAgree = (TextView) convertView.findViewById(R.id.disagree);
//        holder.btnAgree.setOnClickListener(new AgreselickEvent());
//        holder.btnDisAgree.setOnClickListener(new DisAgreselickEvent());
        return convertView;
    }


    static class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView tvDisAgree;
    }

    private class AgreselickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ((Activity) context).finish();
        }
    }

    private class DisAgreselickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ((Activity) context).finish();
        }
    }

}
