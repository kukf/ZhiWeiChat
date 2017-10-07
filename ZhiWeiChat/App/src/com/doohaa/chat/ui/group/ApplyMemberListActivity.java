package com.doohaa.chat.ui.group;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import com.doohaa.chat.R;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.model.GroupMember2;
import com.doohaa.chat.model.Image;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.view.LoadMoreListView;
import com.doohaa.chat.utils.GlobalFunction;
import com.doohaa.chat.utils.ImageLoaderHelper;
import com.google.gson.Gson;
import com.hyphenate.easeui.widget.EaseTitleBar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;

public class ApplyMemberListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView listView;
    private ApplyMemberListAdapter adapter;
    private ImageLoaderHelper imageLoaderHelper;
    private ArrayList<GroupMember2> datas;
    private EaseTitleBar easeTitleBar;
    private Group group;
    private List<GroupMember2> agms = new ArrayList<>();
    private int startNum = 0;
    private int numPerPage = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_apply_member_list);
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        group = (Group)getIntent().getSerializableExtra("item");
        datas = new ArrayList<>();
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new ApplyMemberListAdapter(this, datas);
        easeTitleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        listView = (LoadMoreListView) findViewById(R.id.list);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        listView.setAdapter(adapter);
        listView.setEnableLoadMore(true);
        listView.setOnLoadMoreListener(onLoadMoreListener);
        swipeLayout.setOnRefreshListener(this);
    }

    LoadMoreListView.OnLoadMoreListener onLoadMoreListener = new LoadMoreListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            //loadMoreCompleted();
            startNum = agms.size();
            fetchData();
        }
    };

    private void loadMoreCompleted() {
        if (listView != null) {
            listView.loadMoreCompleted();
        }
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
        String url = "http://jinronghui.wang:8080/publics/group/applyList.do";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("groupId",group.getId()+"");
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token",token);
        params.addBodyParameter("startNum",startNum+"");
        params.addBodyParameter("numPerPage",numPerPage+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String data = jsObj.optString("data");
                    JSONArray jsAry = new JSONArray(data);
                    Gson gson = new Gson();
                    for (int i = 0; i < jsAry.length(); i++) {
                        String obj = jsAry.getString(i);
                        JSONObject jObj = new JSONObject(obj);
                        GroupMember2 gm = new GroupMember2();
                        gm.setFkMemberId(jObj.optInt("fkMemberId"));
                        gm.setType(jObj.optInt("type"));
                        gm.setCreateTime(jObj.optLong("createTime"));
                        gm.setName(jObj.optString("name"));
                        String imgs = jObj.optString("imgProperty");
                        Image img = gson.fromJson(imgs,Image.class);
                        gm.setUrl(img.getUrl());
                        agms.add(gm);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(ApplyMemberListActivity.this,"获取数据失败，请稍后重试...");
                }
                handler.sendEmptyMessage(0);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ApplyMemberListActivity.this,"获取数据失败，请稍后重试...");
                handler.sendEmptyMessage(0);
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    loadMoreCompleted();
                    swipeLayout.setRefreshing(false);
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        };
    };


    @Override
    public void onRefresh() {
        agms.clear();
        adapter.notifyDataSetChanged();
        startNum = 0;
        fetchData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
