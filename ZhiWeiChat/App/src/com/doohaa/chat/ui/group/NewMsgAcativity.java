package com.doohaa.chat.ui.group;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.doohaa.chat.R;
import com.doohaa.chat.api.dto.NewMsg;
import com.doohaa.chat.ui.common.BaseActivity;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.util.ArrayList;


public class NewMsgAcativity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<NewMsg> datas;
    private ListView listView;
    private LayoutInflater layoutInflater;
    private MyAdapter adapter;
    private EaseTitleBar easeTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_new_msg);
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        datas = new ArrayList<>();
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new MyAdapter();
        listView = (ListView) findViewById(R.id.list);
        easeTitleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        listView.setAdapter(adapter);

    }

    private void registEvent() {
        easeTitleBar.setLeftLayoutClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
        }
    }

    private void fetchData() {
//        showProgressDialog();
//        Response.Listener<TradeHistoryDto> successListener = new Response.Listener<TradeHistoryDto>() {
//            @Override
//            public void onResponse(TradeHistoryDto response) {
//                hideProgressDialog();
//                if (Validator.isNotEmpty(response)) {
//                    datas = response.getData();
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        };
//
//        Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                hideProgressDialog();
//            }
//        };
//
//        ApiHelper.dealHistory(TradeHistoryActivity.this, 0, TradeHistoryDto.class, successListener, errorListener);

    }

    @Override
    public void onRefresh() {

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
//            return datas.get(position);
            return position;
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
                convertView = layoutInflater.inflate(R.layout.item_newmsg, null);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }

    static class ViewHolder {

    }
}
