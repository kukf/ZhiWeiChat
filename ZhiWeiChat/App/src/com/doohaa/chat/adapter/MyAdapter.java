package com.doohaa.chat.adapter;

/**
 * Created by iRichardn on 2017/9/14.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doohaa.chat.R;
import com.doohaa.chat.model.Group;
import com.lling.photopicker.utils.ImageManager;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private List<Group> groups;
    private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);
    public MyAdapter(Context context,List<Group> groups){
        this.context = context;
        this.groups = groups;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
//            return bulletins.size();
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
//            return bulletins.get(position);
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Group group = groups.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_mygroup, null);
            holder.ivAvatar = (ImageView)convertView.findViewById(R.id.img);
            holder.tvName = (TextView)convertView.findViewById(R.id.group_name);
            holder.tvAuth = (TextView)convertView.findViewById(R.id.auth);
            holder.tvDesc = (TextView)convertView.findViewById(R.id.des);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        manager.displayImage(holder.ivAvatar,"http://"+group.getImage().getUrl(), ImageManager.ShowType.URL);
        holder.tvName.setText(group.getName());
        holder.tvAuth.setText(group.getCertification());
        holder.tvDesc.setText(group.getDescription());
        return convertView;
    }

    class ViewHolder {
        ImageView ivAvatar;
        TextView tvName;
        TextView tvAuth;
        TextView tvDesc;
    }

}
