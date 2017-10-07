package com.doohaa.chat.ui.article;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.doohaa.chat.R;
import com.doohaa.chat.model.NoteMessage;
import com.doohaa.chat.utils.GlobalFunction;
import java.util.List;

class ReplyDetailAdapter extends RecyclerView.Adapter<ReplyDetailAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<NoteMessage> datas;
    private Context context;

    public ReplyDetailAdapter(Context context, List<NoteMessage> datas) {
        this.context = context;
        this.datas = datas;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reply_detail, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int pos = position;
        NoteMessage nm = datas.get(pos);
        holder.tvContent.setText(nm.getMessage());
        holder.tvName.setText(nm.getMemberName());
        holder.tvTime.setText(GlobalFunction.getDate(nm.getCreateTime()));
        holder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDel;
        TextView tvName;
        TextView tvContent;
        TextView tvTime;

        public MyViewHolder(View convertView) {
            super(convertView);
            tvDel = (TextView)convertView.findViewById(R.id.tv_del);
            tvName = (TextView)convertView.findViewById(R.id.tv_name);
            tvContent = (TextView)convertView.findViewById(R.id.tv_content);
            tvTime = (TextView)convertView.findViewById(R.id.time);
        }
    }
}
