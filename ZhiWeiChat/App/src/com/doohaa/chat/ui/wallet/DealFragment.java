package com.doohaa.chat.ui.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.doohaa.chat.R;
import com.doohaa.chat.api.ApiHelper;
import com.doohaa.chat.api.dto.ApiResponse;
import com.doohaa.chat.api.dto.GetProductsDataDto;
import com.doohaa.chat.api.dto.GetProductsDto;
import com.doohaa.chat.api.dto.ProduceAbleDto;
import com.doohaa.chat.api.dto.ProduceAbleResultDto;
import com.doohaa.chat.api.dto.ProductDto;
import com.doohaa.chat.api.dto.SearchUserDto;
import com.doohaa.chat.api.dto.UserDto;
import com.doohaa.chat.preferences.UserPreferences;
import com.doohaa.chat.ui.EventBus.BaseEvent;
import com.doohaa.chat.ui.EventBus.EventType;
import com.doohaa.chat.ui.EventBus.ModifyUserEvent;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.common.BaseFragment;
import com.doohaa.chat.ui.common.OCToast;
import com.doohaa.chat.utils.Validator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by sunshixiong on 6/8/16.
 */
public class DealFragment extends BaseFragment implements View.OnClickListener, TextWatcher {
    private LayoutInflater inflater;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout llytErrorPage;
    private ScrollView scytContainer;
    private Button btnRetry;

    private Spinner spProduct;
    private Spinner spOperate;
    private TextView txtPrice;
    private TextView txtAllPrice;
    private EditText edtCount;
    private TextView txtMaxCount;
    private CheckBox chkConfirm;
    private Button btnConfirm;
    private Button btnMoreTradeHistory;
    private LinearLayout llytMaxCountContainer;
    private TextView txtMaxCountTitle;

    private ArrayList<ProductDto> productDtos;
    private Map<String, Integer> memberProducts;
    private int operatePos = 0;
    private int productPos = 0;

    private int isAble = 0;

    public static DealFragment newInstance() {
        return new DealFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.em_fragment_wallet_deal, container, false);
        initComponent(rootView);
        registEvent();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    }

    private void initComponent(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        llytErrorPage = (LinearLayout) rootView.findViewById(R.id.error_page);
        scytContainer = (ScrollView) rootView.findViewById(R.id.container);
        btnRetry = (Button) rootView.findViewById(R.id.retry);
        spProduct = (Spinner) rootView.findViewById(R.id.products);
        spOperate = (Spinner) rootView.findViewById(R.id.operate_select);
        txtPrice = (TextView) rootView.findViewById(R.id.price);
        txtAllPrice = (TextView) rootView.findViewById(R.id.all_price);
        edtCount = (EditText) rootView.findViewById(R.id.count);
        txtMaxCount = (TextView) rootView.findViewById(R.id.max_count);
        chkConfirm = (CheckBox) rootView.findViewById(R.id.confirm_operate);
        btnConfirm = (Button) rootView.findViewById(R.id.confirm);
        btnMoreTradeHistory = (Button) rootView.findViewById(R.id.more_trade);
        llytMaxCountContainer = (LinearLayout) rootView.findViewById(R.id.max_count_container);
        txtMaxCountTitle = (TextView) rootView.findViewById(R.id.max_count_title);
    }

    private void registEvent() {
        btnConfirm.setOnClickListener(this);
        btnMoreTradeHistory.setOnClickListener(this);
        edtCount.addTextChangedListener(this);
        btnRetry.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            fetchData();
        }
    };

    private void fetchData() {
        ((BaseActivity) getActivity()).showProgressDialog();
        Response.Listener<SearchUserDto> successListener = new Response.Listener<SearchUserDto>() {
            @Override
            public void onResponse(SearchUserDto response) {
                if (Validator.isNotEmpty(response)) {
                    if (Validator.isNotEmpty(response)) {
                        UserDto userDto = response.getData();
                        if (Validator.isNotEmpty(userDto)) {
                            hiddenErrorPage();
                            UserPreferences.getInstance(getActivity()).setUser(userDto);
                            EventBus.getDefault().post(new ModifyUserEvent(EventType.MODIFY_USER, userDto));
                            fetchProductData();
                        } else {
                            ((BaseActivity) getActivity()).hideProgressDialog();
                            swipeRefreshLayout.setRefreshing(false);
                            showErrorPage();
                        }
                    } else {
                        ((BaseActivity) getActivity()).hideProgressDialog();
                        swipeRefreshLayout.setRefreshing(false);
                        showErrorPage();
                    }
                } else {
                    ((BaseActivity) getActivity()).hideProgressDialog();
                    swipeRefreshLayout.setRefreshing(false);
                    showErrorPage();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                showErrorPage();
            }
        };
        chkConfirm.setChecked(false);
        ApiHelper.getUserProfile(getActivity(), "", "", SearchUserDto.class, successListener, errorListener);
    }

    public void fetchProductData() {
        Response.Listener<GetProductsDto> successListener = new Response.Listener<GetProductsDto>() {
            @Override
            public void onResponse(GetProductsDto response) {
                if (Validator.isNotEmpty(response)) {
                    GetProductsDataDto dataDto = response.getData();
                    if (Validator.isNotEmpty(dataDto)) {
                        hiddenErrorPage();
                        productDtos = dataDto.getProducts();
                        memberProducts = dataDto.getMemberProducts();
                        constructOperateAdapter();
                        constructProductAdapter();
                        fetchProduceAble();
                    } else {
                        ((BaseActivity) getActivity()).hideProgressDialog();
                        swipeRefreshLayout.setRefreshing(false);
                        showErrorPage();
                    }
                } else {
                    ((BaseActivity) getActivity()).hideProgressDialog();
                    swipeRefreshLayout.setRefreshing(false);
                    showErrorPage();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                showErrorPage();
            }
        };

        ApiHelper.getProducts(getActivity(), GetProductsDto.class, successListener, errorListener);
    }

    private void fetchProduceAble() {
        Response.Listener<ProduceAbleDto> successListener = new Response.Listener<ProduceAbleDto>() {
            @Override
            public void onResponse(ProduceAbleDto response) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (Validator.isNotEmpty(response)) {
                    ProduceAbleResultDto responseData = response.getData();
                    if (Validator.isNotEmpty(responseData)) {
                        hiddenErrorPage();
                        isAble = responseData.getAble();
                        setText();
                        if (isAble > 3 || isAble < 0) {
                            showErrorPage();
                        }
                    } else {
                        showErrorPage();
                    }
                } else {
                    showErrorPage();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                showErrorPage();
            }
        };

        ApiHelper.getProduceAble(getActivity(), ProduceAbleDto.class, successListener, errorListener);
    }

    public void buyProduct() {
        ((BaseActivity) getActivity()).showProgressDialog();
        Response.Listener<ApiResponse> successListener = new Response.Listener<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse response) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                hiddenErrorPage();
                OCToast.makeToast(getActivity(), R.string.buy_success, Toast.LENGTH_LONG).show();
                EventBus.getDefault().post(new ModifyUserEvent(EventType.MODIFY_USER, null));
                fetchData();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                showErrorPage();
                OCToast.makeToast(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        ProductDto productDto = productDtos.get(productPos);
        long productId = productDto.getId();

        ApiHelper.buyProduct(getActivity(), productId, edtCount.getText().toString(), ApiResponse.class, successListener, errorListener);
    }

    public void sellProduct() {
        ((BaseActivity) getActivity()).showProgressDialog();
        Response.Listener<ApiResponse> successListener = new Response.Listener<ApiResponse>() {
            @Override
            public void onResponse(ApiResponse response) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                EventBus.getDefault().post(new ModifyUserEvent(EventType.MODIFY_USER, null));
                OCToast.makeToast(getActivity(), R.string.sell_success, Toast.LENGTH_LONG).show();
                hiddenErrorPage();
                fetchData();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                showErrorPage();
                OCToast.makeToast(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        ProductDto productDto = productDtos.get(productPos);
        long productId = productDto.getId();

        ApiHelper.sellProduct(getActivity(), productId, edtCount.getText().toString(), ApiResponse.class, successListener, errorListener);
    }

    private void constructOperateAdapter() {
        String[] items = new String[2];
        items[0] = getActivity().getResources().getString(R.string.buy);
        items[1] = getActivity().getResources().getString(R.string.sell);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOperate.setAdapter(adapter);
        spOperate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                onOperateSelected(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void constructProductAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getProductsNameArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProduct.setAdapter(adapter);
        spProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                onProductSelected(pos);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private String[] getProductsNameArray() {
        if (Validator.isEmpty(productDtos)) {
            return new String[0];
        }

        int size = productDtos.size();
        String[] items = new String[size];
        for (int i = 0; i < size; i++) {
            items[i] = productDtos.get(i).getName();
        }
        return items;
    }

    private void onOperateSelected(int pos) {
        operatePos = pos;
        if (Validator.isEmpty(productDtos)) {
            return;
        }
        ProductDto productDto = productDtos.get(productPos);
        switch (pos) {
            case 0:
                txtMaxCountTitle.setText(R.string.max_count_buy);

                BigDecimal bgMoney = UserPreferences.getInstance(getActivity()).getUser().getMoney();
                BigDecimal bdPrice = productDto.getPrice();
                int maxBuyCount = 0;
                int price = 0;
                if (bdPrice.compareTo(new BigDecimal(0)) > 0) {
                    price = bdPrice.toBigInteger().intValue();
                }
                if (price == 0) {
                    OCToast.makeToast(getActivity(), R.string.price_error, Toast.LENGTH_SHORT).show();
                } else {
                    maxBuyCount = bgMoney.divide(bdPrice, 0, BigDecimal.ROUND_DOWN).intValue();
                }
                txtMaxCount.setText(String.valueOf(maxBuyCount));
                break;

            case 1:
                txtMaxCountTitle.setText(R.string.max_count_sell);
                String productId = productDto.getId() + "";
                txtMaxCount.setText(memberProducts.containsKey(productId) ? String.valueOf(memberProducts.get(productId)) : "0");
                break;
        }
        setText();
    }

    private void onProductSelected(int pos) {
        productPos = pos;
        if (Validator.isEmpty(productDtos)) {
            return;
        }
        ProductDto productDto = productDtos.get(pos);
        txtPrice.setText(String.valueOf(productDto.getPrice()));
        edtCount.setText("");
        switch (operatePos) {
            case 0:
                BigDecimal bgMoney = UserPreferences.getInstance(getActivity()).getUser().getMoney();
                BigDecimal bdPrice = productDto.getPrice();
                int maxBuyCount = 0;
                int price = 0;
                if (bdPrice.compareTo(new BigDecimal(0)) > 0) {
                    price = bdPrice.toBigInteger().intValue();
                }
                if (price == 0) {
                    OCToast.makeToast(getActivity(), R.string.price_error, Toast.LENGTH_SHORT).show();
                } else {
                    maxBuyCount = bgMoney.divide(bdPrice, 0, BigDecimal.ROUND_DOWN).intValue();
                }
                txtMaxCount.setText(String.valueOf(maxBuyCount));
                break;

            case 1:
                String productId = productDto.getId() + "";
                txtMaxCount.setText(memberProducts.containsKey(productId) ? String.valueOf(memberProducts.get(productId)) : "0");
                break;
        }
    }

    private void setText() {
        switch (operatePos) {
            case 0:
                if (isAble == 0 || isAble == 2) {
                    btnConfirm.setText(R.string.btn_unable_buy);
                } else {
                    btnConfirm.setText(R.string.buy);
                }
                break;

            case 1:
                if (isAble == 0 || isAble == 1) {
                    btnConfirm.setText(R.string.btn_unable_sell);
                } else {
                    btnConfirm.setText(R.string.sell);
                }
                break;
        }
    }

    private void onConfirmClick() {
        switch (operatePos) {
            case 0:
                if (isAble == 0 || isAble == 2) {
                    OCToast.makeToast(getActivity(), R.string.unable_buy, Toast.LENGTH_SHORT).show();
                    return;
                }
                break;

            case 1:
                if (isAble == 0 || isAble == 1) {
                    OCToast.makeToast(getActivity(), R.string.unable_sell, Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }


        String count = edtCount.getText().toString().trim();
        if (Validator.isEmpty(count)) {
            OCToast.makeToast(getActivity(), R.string.input_count_plz, Toast.LENGTH_SHORT).show();
            return;
        }

        int countInt = Integer.parseInt(count);
        if (countInt < 1) {
            OCToast.makeToast(getActivity(), R.string.input_count_plz, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!chkConfirm.isChecked()) {
            OCToast.makeToast(getActivity(), R.string.check_confirm_operate, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (operatePos) {

            case 0:
                BigDecimal bgMoney = UserPreferences.getInstance(getActivity()).getUser().getMoney();
                BigDecimal allPrice = new BigDecimal(txtAllPrice.getText().toString());
                if (bgMoney.compareTo(allPrice) < 0) {

                    OCToast.makeToast(getActivity(), getString(R.string.no_enough_money) + txtMaxCount.getText().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    buyProduct();
                }
                break;

            case 1:
                String maxCount = txtMaxCount.getText().toString().trim();

                int maxCountInt = Integer.parseInt(maxCount);
                if (countInt > maxCountInt) {
                    OCToast.makeToast(getActivity(), getString(R.string.exceed_max_count) + maxCount, Toast.LENGTH_SHORT).show();
                } else {
                    sellProduct();
                }
                break;
        }
    }

    private void onMoreTradeHistory() {
        startActivity(new Intent(getActivity(), TradeHistoryActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                onConfirmClick();
                break;

            case R.id.more_trade:
                onMoreTradeHistory();
                break;

            case R.id.retry:
                fetchData();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (Validator.isEmpty(s.toString())) {
            txtAllPrice.setText("0");
            return;
        }
        try {
            int count = Integer.valueOf(s.toString());
            ProductDto productDto = productDtos.get(productPos);
            BigDecimal price = productDto.getPrice();
            BigDecimal decCount = new BigDecimal(count);
            txtAllPrice.setText(String.valueOf(price.multiply(decCount)));
        } catch (Exception e) {

        }
    }

    private void showErrorPage() {
        llytErrorPage.setVisibility(View.VISIBLE);
        scytContainer.setVisibility(View.GONE);
    }

    private void hiddenErrorPage() {
        llytErrorPage.setVisibility(View.GONE);
        scytContainer.setVisibility(View.VISIBLE);
    }
}
