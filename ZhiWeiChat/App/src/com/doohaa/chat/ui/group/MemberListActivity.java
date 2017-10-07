package com.doohaa.chat.ui.group;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.doohaa.chat.R;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.model.GroupMember2;
import com.doohaa.chat.model.Image;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
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

public class MemberListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private MemberListAdapter adapter;
    private ImageLoaderHelper imageLoaderHelper;
    private ArrayList<GroupMember2> datas;
    private EaseTitleBar easeTitleBar;
    private Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_member_list);
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        group = (Group)getIntent().getSerializableExtra("item");
        datas = new ArrayList<>();
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new MemberListAdapter(this, datas);
        easeTitleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        listView = (ListView) findViewById(R.id.list);
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
        String url = "http://jinronghui.wang:8080/publics/group/memberList.do";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("groupId",group.getId()+"");
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token",token);
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
                        datas.add(gm);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(MemberListActivity.this,"操作失败，请稍后重试...");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(MemberListActivity.this,"操作失败，请稍后重试...");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }


    public void kick(final int pos) {
        GroupMember2 m = datas.get(pos);
        String url = "http://jinronghui.wang:8080/publics/group/knick.do";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("groupId",group.getId()+"");
        params.addBodyParameter("knickMemberId",m.getFkMemberId()+"");
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        datas.remove(pos);
                        adapter.notifyDataSetChanged();
                        GlobalFunction.showToast(MemberListActivity.this,"踢人成功。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(MemberListActivity.this,"操作失败，请稍后重试...");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(MemberListActivity.this,"操作失败，请稍后重试...");
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
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
