package com.doohaa.chat.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.doohaa.chat.R;
import com.lling.photopicker.utils.ImageManager;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<String> list;
	private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);
	public ImageAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.fx_item_gridview_image, null);
		ImageView sdvImage = (ImageView) convertView
				.findViewById(R.id.sdv_image);
		final ImageView ivClose = (ImageView) convertView
				.findViewById(R.id.iv_close);
		String path = list.get(position);
		if(TextUtils.isEmpty(path)){
			manager.displayImage(sdvImage, R.drawable.em_add);
		}else{
			manager.displayImage(sdvImage, path, ImageManager.ShowType.FILE);
		}
		ivClose.setTag(position);
		ivClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int pos = (int)v.getTag();
				list.remove(pos);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
}
