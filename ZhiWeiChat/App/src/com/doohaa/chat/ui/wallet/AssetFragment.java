package com.doohaa.chat.ui.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.MemberProductDataDto;
import com.doohaa.chat.api.dto.MemberProductDto;
import com.doohaa.chat.api.dto.ProductDto;
import com.doohaa.chat.api.dto.SearchUserDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.EventBus.BaseEvent;
import com.doohaa.chat.ui.EventBus.ModifyUserEvent;
import com.doohaa.chat.ui.common.BaseFragment;
import com.doohaa.chat.ui.own.RechargeActivity;
import com.doohaa.chat.utils.Validator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by sunshixiong on 6/8/16.
 */
public class AssetFragment extends BaseFragment implements View.OnClickListener {
    public static AssetFragment newInstance() {
        return new AssetFragment();
    }

    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtBalance;
    private Button btnRecharge;
    private Button btnGetCash;
    private TextView txtRechargeHistory;
    private TextView txtWithDrawalsHistory;
    private ListView listView;
    private MyAdapter adapter;
    private ArrayList<MemberProductDataDto> datas;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.em_fragment_wallet_asset, container, false);
        initComponent(rootView);
        registEvent();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fetchData();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    /******
     * EVENT BUS
     ******/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case MODIFY_USER:
                if (event instanceof ModifyUserEvent) {
                    fetchData();
                }
                break;
        }
    }


    private void initComponent(View rootView) {
        datas = new ArrayList<>();
        adapter = new MyAdapter();

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        txtBalance = (TextView) rootView.findViewById(R.id.balance);
        btnRecharge = (Button) rootView.findViewById(R.id.recharge);
        btnGetCash = (Button) rootView.findViewById(R.id.get_cash);
        txtRechargeHistory = (TextView) rootView.findViewById(R.id.recharge_history);
        txtWithDrawalsHistory = (TextView) rootView.findViewById(R.id.getcash_history);
        listView = (ListView) rootView.findViewById(R.id.listview);

        listView.setAdapter(adapter);
    }

    private void registEvent() {
        btnRecharge.setOnClickListener(this);
        btnGetCash.setOnClickListener(this);
        txtRechargeHistory.setOnClickListener(this);
        txtWithDrawalsHistory.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            fetchData();
        }
    };

    public void fetchMemberProduct() {
        Response.Listener<MemberProductDto> successListener = new Response.Listener<MemberProductDto>() {
            @Override
            public void onResponse(MemberProductDto response) {
                swipeRefreshLayout.setRefreshing(false);
                if (Validator.isNotEmpty(response)) {
                    datas = response.getData();
                    adapter.notifyDataSetChanged();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        ApiHelper.getMemberProducts(getActivity(), MemberProductDto.class, successListener, errorListener);
    }

    private void fetchData() {
        Response.Listener<SearchUserDto> successListener = new Response.Listener<SearchUserDto>() {
            @Override
            public void onResponse(SearchUserDto response) {
                if (Validator.isNotEmpty(response)) {
                    if (Validator.isNotEmpty(response)) {
                        UserDto userDto = response.getData();
                        if (Validator.isNotEmpty(userDto)) {
                            UserPreferences.getInstance(getActivity()).setUser(userDto);
                            txtBalance.setText(userDto.getMoney().toString());
                        }
                    }
                }

                fetchMemberProduct();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        ApiHelper.getUserProfile(getActivity(), "", "", SearchUserDto.class, successListener, errorListener);
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
                convertView = layoutInflater.inflate(R.layout.item_member_product, null);
                holder.productName = (TextView) convertView.findViewById(R.id.product_name);
                holder.count = (TextView) convertView.findViewById(R.id.count);
                holder.price = (TextView) convertView.findViewById(R.id.price1);
                holder.allPrice = (TextView) convertView.findViewById(R.id.all_price1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MemberProductDataDto memberProductDataDto = datas.get(position);
            long count = memberProductDataDto.getQuantity();
            ProductDto productDto = memberProductDataDto.getProduct();
            String productName = productDto.getName();
            BigDecimal bdPrice = productDto.getPrice();
            BigDecimal bdAllPrice = bdPrice.multiply(new BigDecimal(count));

            holder.productName.setText(productName);
            holder.count.setText(count + "");
            holder.price.setText(bdPrice.toString());
            holder.allPrice.setText(bdAllPrice.toString());
            return convertView;
        }
    }

    static class ViewHolder {
        TextView productName;
        TextView count;
        TextView price;
        TextView allPrice;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge:
                startActivity(new Intent(getActivity(), RechargeActivity.class));
                break;

            case R.id.get_cash:
                startActivity(new Intent(getActivity(), WithDrawalsActivity.class).putExtra("fromCode", 0));
                break;

            case R.id.getcash_history:
                startActivity(new Intent(getActivity(), WithDrawalsActivity.class).putExtra("fromCode", 1));
                break;

            case R.id.recharge_history:
                startActivity(new Intent(getActivity(), WithDrawalsActivity.class).putExtra("fromCode", 2));
                break;
        }
    }
}
