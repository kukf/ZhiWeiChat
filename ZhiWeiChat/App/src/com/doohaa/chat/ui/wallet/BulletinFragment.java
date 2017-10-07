package com.doohaa.chat.ui.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.Bulletin;
import com.doohaa.chat.api.dto.BulletinListDto;
import com.doohaa.chat.api.dto.ImgPropertyDto;
import com.doohaa.chat.ui.common.BaseFragment;
import com.doohaa.chat.utils.ImageLoaderHelper;
import com.doohaa.chat.utils.UIUtils;
import com.doohaa.chat.utils.Validator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunshixiong on 6/8/16.
 */
public class BulletinFragment extends BaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static BulletinFragment newInstance() {
        return new BulletinFragment();
    }

    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipe_layout;
    private ListView listView;
    private MyAdapter adapter;
    private ImageLoaderHelper imageLoaderHelper;
    private View rootView;
    private ArrayList<Bulletin> bulletins;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.em_fragment_wallet_bulletin, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        bulletins = new ArrayList<>();
        adapter = new MyAdapter();
        imageLoaderHelper = new ImageLoaderHelper(getActivity());
        swipe_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        swipe_layout.setOnRefreshListener(this);

    }

    private void registEvent() {
        listView.setOnItemClickListener(this);
    }

    public String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    private void fetchData() {
        Response.Listener<BulletinListDto> successListener = new Response.Listener<BulletinListDto>() {
            @Override
            public void onResponse(BulletinListDto response) {
                swipe_layout.setRefreshing(false);
                if (Validator.isNotEmpty(response)) {
                    bulletins = response.getData();
                    adapter.notifyDataSetChanged();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipe_layout.setRefreshing(false);
            }
        };

        ApiHelper.bulletinList(getActivity(), BulletinListDto.class, successListener, errorListener);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), BulletinActivity.class);
        intent.putExtra("id", bulletins.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bulletins.size();
        }

        @Override
        public Object getItem(int position) {
            return bulletins.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.item_bulletin, null);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                holder.title = (TextView) convertView.findViewById(R.id.bulletin_title);
                holder.content = (TextView) convertView.findViewById(R.id.bulletin_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Bulletin bulletin = bulletins.get(position);
            ImgPropertyDto imgProperty = bulletin.getImgProperty();
            String title = bulletin.getTitle();
            String content = bulletin.getBody();
            holder.title.setText(title);
            holder.content.setText(replaceBlank(content));
            if (Validator.isNotEmpty(imgProperty)) {
                holder.img.setVisibility(View.VISIBLE);
                String url = UIUtils.getImageUrl(imgProperty);
                imageLoaderHelper.loadImage(url, R.drawable.default_load_img, holder.img);
            } else {
                holder.img.setVisibility(View.GONE);
            }

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView img;
        TextView title;
        TextView content;
    }
}
