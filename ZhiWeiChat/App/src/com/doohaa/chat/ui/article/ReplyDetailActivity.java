package com.doohaa.chat.ui.article;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.doohaa.chat.R;
import com.doohaa.chat.model.NoteMessage;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.utils.DividerItemDecoration;
import com.doohaa.chat.utils.GlobalFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.widget.EaseTitleBar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ReplyDetailActivity extends BaseActivity implements View.OnClickListener {
    private LayoutInflater layoutInflater;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout materialRefreshLayout;
    private ReplyDetailAdapter adapter;
    private List<NoteMessage> datas;
    private View headerRootView;
    private EaseTitleBar easeTitleBar;
    private int numPerPage = 10;
    private int startNum = 0;
    private NoteMessage nm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_reply_list);
        nm = (NoteMessage)getIntent().getSerializableExtra("item");
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        datas = new ArrayList<>();
        headerRootView = new ReplyDetailHeader(this);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new ReplyDetailAdapter(this, datas);
        easeTitleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh_layout);
        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                startNum = datas.size();
                getComments();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        llContent.addView(headerRootView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    materialRefreshLayout.finishRefresh();
                    break;
                case 2:
                    materialRefreshLayout.finishRefreshLoadMore();
                    break;
                default:
                    break;
            }
        }
    };


    private void registEvent() {
        easeTitleBar.setLeftLayoutClickListener(this);
    }

    /**
     * 回复主题
     */
    private void getComments() {
        String url = "http://jinronghui.wang:8080/publics/note/message/query.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token", token);
        params.addBodyParameter("noteIdList", nm.getId() + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String data = jsObj.optString("data");
                    JSONArray jsAry = new JSONArray(data);
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<NoteMessage>>() {
                    }.getType();
                    for (int i = 0; i < jsAry.length(); i++) {
                        String obj = jsAry.getString(i);
                        JSONObject jObj = new JSONObject(obj);
                        String msgs = jObj.optString("messageList");
                        List<NoteMessage> msg = gson.fromJson(msgs,type);
                        datas.addAll(msg);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ReplyDetailActivity.this, R.string.get_data_failure);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
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
        getComments();
    }
}
