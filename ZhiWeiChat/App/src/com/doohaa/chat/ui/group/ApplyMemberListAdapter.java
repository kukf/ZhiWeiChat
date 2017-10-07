package com.doohaa.chat.ui.group;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.doohaa.chat.R;
import com.doohaa.chat.model.GroupMember2;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.utils.GlobalFunction;
import com.google.gson.Gson;
import com.lling.photopicker.utils.ImageManager;

import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.ArrayList;

class ApplyMemberListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<GroupMember2> datas;
    private Context context;
    private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);
    public ApplyMemberListAdapter(Context context, ArrayList<GroupMember2> datas) {
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
        GroupMember2 member = datas.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_apply_member_list, null);
            holder.tvAgree = (TextView) convertView.findViewById(R.id.agree);
            holder.tvDisagree = (TextView) convertView.findViewById(R.id.disagree);
            holder.avatar = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.user_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GroupMember2 member = datas.get(pos);
                agree(member,1);
            }
        });
        holder.tvDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GroupMember2 member = datas.get(pos);
                agree(member,2);
            }
        });
        manager.displayImage(holder.avatar,"http://"+member.getUrl(), ImageManager.ShowType.URL);
        holder.name.setText(member.getName());
        return convertView;
    }

    private void agree(GroupMember2 member,int action) {
        String url = "http://jinronghui.wang:8080/publics/group/approve.do";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("id",member.getId()+"");
        params.addBodyParameter("type",action+"");
        String token = TokenPreferences.getInstance(context).getToken();
        params.addBodyParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONArray jsAry = new JSONArray(result);
                    Gson gson = new Gson();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(context,"操作失败，请稍后重试。");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    static class ViewHolder {
        TextView name;
        ImageView avatar;
        TextView tvAgree;
        TextView tvDisagree;
    }

}
