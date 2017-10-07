package com.doohaa.chat.ui.wallet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.TradeHistory;
import com.doohaa.chat.api.dto.TradeHistoryDto;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.utils.UIUtils;
import com.doohaa.chat.utils.Validator;


import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by LittleBear on 2016/6/16.
 */
public class TradeHistoryActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<TradeHistory> datas;
    private ListView listView;
    private LayoutInflater layoutInflater;
    private MyAdapter adapter;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_tradehistory);
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        datas = new ArrayList<>();
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new MyAdapter();
        imgBack = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

    }

    private void registEvent() {
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void fetchData() {
        showProgressDialog();
        Response.Listener<TradeHistoryDto> successListener = new Response.Listener<TradeHistoryDto>() {
            @Override
            public void onResponse(TradeHistoryDto response) {
                hideProgressDialog();
                if (Validator.isNotEmpty(response)) {
                    datas = response.getData();
                    adapter.notifyDataSetChanged();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
            }
        };

        ApiHelper.dealHistory(TradeHistoryActivity.this, 0, TradeHistoryDto.class, successListener, errorListener);

    }

    private class MyAdapter extends BaseAdapter {

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
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.trade_item_for_activity, null);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.product_name);
                holder.txtTime = (TextView) convertView.findViewById(R.id.time);
                holder.txtOperate = (TextView) convertView.findViewById(R.id.operate);
                holder.txtPrice = (TextView) convertView.findViewById(R.id.price);
                holder.txtCount = (TextView) convertView.findViewById(R.id.count);
                holder.txtAllPrice = (TextView) convertView.findViewById(R.id.allPrice);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TradeHistory tradeHistory = datas.get(position);
            int type = tradeHistory.getType();
            String product = tradeHistory.getProduct().getName();
            BigDecimal price = tradeHistory.getPrice();
            int count = tradeHistory.getQuantity();
            long time = tradeHistory.getDealTime();

            holder.txtTitle.setText(product == null ? "" : product);
            holder.txtTime.setText(UIUtils.timestamp2Date(time));
            switch (type) {
                case 1:
                    holder.txtOperate.setText("买入");
                    holder.txtAllPrice.setText("- " + price.multiply(new BigDecimal(count)).toString());
                    break;

                case 2:
                    holder.txtOperate.setText("卖出");
                    holder.txtAllPrice.setText("+ " + price.multiply(new BigDecimal(count)).toString());
                    break;
            }
            holder.txtCount.setText(count + "");
            holder.txtPrice.setText(price.toString());

            return convertView;
        }
    }

    static class ViewHolder {
        TextView txtTitle;
        TextView txtTime;
        TextView txtOperate;
        TextView txtPrice;
        TextView txtCount;
        TextView txtAllPrice;
    }
}
