package com.doohaa.chat.ui.group;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.doohaa.chat.R;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.model.GroupMember2;
import com.doohaa.chat.model.Image;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.article.ArticleListActivity;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.utils.GlobalFunction;
import com.google.gson.Gson;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.lling.photopicker.utils.ImageManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;

public class GroupDetailActivity extends BaseActivity implements View.OnClickListener {
    private EaseTitleBar easeTitleBar;
    private TextView txtMemberMore;
    private TextView tvGroupName;
    private TextView tvAuth;
    private TextView tvDesc;
    private TextView tvMemberCount;
    private TextView tvArticleCount;
    private TextView txtApplyMemberMore;
    private TextView tvGoGroup;
    private TextView tvJoin;
    private ImageView img;
    private LinearLayout ll1;
    private ImageView img1;
    private TextView name1;
    private LinearLayout ll2;
    private ImageView img2;
    private TextView name2;
    private LinearLayout ll3;
    private ImageView img3;
    private TextView name3;
    private LinearLayout ll4;
    private ImageView img4;
    private TextView name4;

    private LinearLayout all1;
    private ImageView aimg1;
    private TextView aname1;
    private LinearLayout all2;
    private ImageView aimg2;
    private TextView aname2;
    private LinearLayout all3;
    private ImageView aimg3;
    private TextView aname3;
    private LinearLayout all4;
    private ImageView aimg4;
    private TextView aname4;

    private Group group;
    private int startNum;
    private int numPerPage;
    private List<GroupMember2> gms = new ArrayList<>();
    private List<GroupMember2> agms = new ArrayList<>();
    private ImageManager manager = new ImageManager(R.drawable.ease_default_avatar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_group_detail);
        group = (Group)getIntent().getSerializableExtra("item");
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        easeTitleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        txtMemberMore = (TextView) findViewById(R.id.member_more);
        img = (ImageView) findViewById(R.id.img);
        manager.displayImage(img,"http://"+group.getImage().getUrl(), ImageManager.ShowType.URL);
        tvGroupName = (TextView) findViewById(R.id.group_name);
        tvAuth = (TextView) findViewById(R.id.auth);
        tvDesc = (TextView) findViewById(R.id.des);
        tvMemberCount = (TextView) findViewById(R.id.member_count);
        tvArticleCount = (TextView) findViewById(R.id.article_count);
        txtApplyMemberMore = (TextView) findViewById(R.id.apply_member_more);
        tvGoGroup = (TextView) findViewById(R.id.go_gruop);
        tvJoin = (TextView) findViewById(R.id.member_state);
        if(group.isIn()){
            tvJoin.setText("已加入");
        }else{
            tvJoin.setText("加入");
        }
        tvGroupName.setText(group.getName());
        tvAuth.setText(group.getCertification());
        tvDesc.setText(group.getDescription());
        tvMemberCount.setText(group.getMemberCount()+"");
        tvArticleCount.setText(group.getNoteCount()+"");

        ll1 = (LinearLayout) findViewById(R.id.ll_1);
        img1 = (ImageView) findViewById(R.id.img1);
        name1 = (TextView) findViewById(R.id.user_name);

        ll2 = (LinearLayout) findViewById(R.id.ll_2);
        img2 = (ImageView) findViewById(R.id.img2);
        name2 = (TextView) findViewById(R.id.user_name2);

        ll3 = (LinearLayout) findViewById(R.id.ll_3);
        img3 = (ImageView) findViewById(R.id.img3);
        name3 = (TextView) findViewById(R.id.user_name3);

        ll4 = (LinearLayout) findViewById(R.id.ll_4);
        img4 = (ImageView) findViewById(R.id.img4);
        name4 = (TextView) findViewById(R.id.user_name4);

        all1 = (LinearLayout) findViewById(R.id.a_ll1);
        aimg1 = (ImageView) findViewById(R.id.apply_img1);
        aname1 = (TextView) findViewById(R.id.apply_user_name);

        all2 = (LinearLayout) findViewById(R.id.a_ll2);
        aimg2 = (ImageView) findViewById(R.id.apply_img2);
        aname2 = (TextView) findViewById(R.id.apply_user_name2);

        all3 = (LinearLayout) findViewById(R.id.a_ll3);
        aimg3 = (ImageView) findViewById(R.id.apply_img3);
        aname3 = (TextView) findViewById(R.id.apply_user_name3);

        all4 = (LinearLayout) findViewById(R.id.a_ll4);
        aimg4 = (ImageView) findViewById(R.id.apply_img4);
        aname4 = (TextView) findViewById(R.id.apply_user_name4);
    }

    private void registEvent() {
        easeTitleBar.setLeftLayoutClickListener(this);
        txtMemberMore.setOnClickListener(this);
        txtApplyMemberMore.setOnClickListener(this);
        tvGoGroup.setOnClickListener(this);
        tvJoin.setOnClickListener(this);
    }


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    group.setIn(!group.isIn());
                    if(group.isIn()){
                        tvJoin.setText("已加入");
                    }else{
                        tvJoin.setText("加入");
                    }
                    break;
                case 1:
                    initGroupList();
                    break;
                case 2:
                    initApplyList();
                    break;
                default:
                    break;
            }
        }
    };


    private void initApplyList() {
        all1.setVisibility(View.GONE);
        all2.setVisibility(View.GONE);
        all3.setVisibility(View.GONE);
        all4.setVisibility(View.GONE);
        if(gms == null || gms.size() == 0){
            return;
        }
        if(agms.size() == 1){
            GroupMember2 gm  = agms.get(0);
            manager.displayImage(aimg1,"http://"+gm.getUrl(), ImageManager.ShowType.URL);
            aname1.setText(gm.getName());
            all1.setVisibility(View.VISIBLE);
        }
        if(agms.size() == 2){
            GroupMember2 gm  = agms.get(1);
            manager.displayImage(aimg2,"http://"+gm.getUrl(), ImageManager.ShowType.URL);
            aname2.setText(gm.getName());
            all2.setVisibility(View.VISIBLE);
        }
        if(agms.size() == 3){
            GroupMember2 gm  = agms.get(2);
            manager.displayImage(aimg3,"http://"+gm.getUrl(), ImageManager.ShowType.URL);
            aname3.setText(gm.getName());
            all3.setVisibility(View.VISIBLE);
        }
        if(agms.size() == 4){
            GroupMember2 gm  = agms.get(3);
            manager.displayImage(aimg4,"http://"+gm.getUrl(), ImageManager.ShowType.URL);
            aname4.setText(gm.getName());
            all4.setVisibility(View.VISIBLE);
        }
    }

    private void initGroupList() {
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        ll4.setVisibility(View.GONE);
        if(gms == null || gms.size() == 0){
            return;
        }
        if(gms.size() == 1){
            GroupMember2 gm  = gms.get(0);
            manager.displayImage(img1,"http://"+gm.getUrl(), ImageManager.ShowType.URL);
            name1.setText(gm.getName());
            ll1.setVisibility(View.VISIBLE);
        }
        if(gms.size() == 2){
            GroupMember2 gm  = gms.get(1);
            manager.displayImage(img2,"http://"+gm.getUrl(), ImageManager.ShowType.URL);
            name2.setText(gm.getName());
            ll2.setVisibility(View.VISIBLE);
        }
        if(gms.size() == 3){
            GroupMember2 gm  = gms.get(2);
            manager.displayImage(img3,"http://"+gm.getUrl(), ImageManager.ShowType.URL);
            name3.setText(gm.getName());
            ll3.setVisibility(View.VISIBLE);
        }
        if(gms.size() == 4){
            GroupMember2 gm  = gms.get(3);
            manager.displayImage(img4,"http://"+gm.getUrl(), ImageManager.ShowType.URL);
            name4.setText(gm.getName());
            ll4.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.member_more:
                Intent intent = new Intent(GroupDetailActivity.this, MemberListActivity.class);
                intent.putExtra("item",group);
                startActivity(intent);
                break;
            case R.id.apply_member_more:
                Intent intent1 = new Intent(GroupDetailActivity.this, ApplyMemberListActivity.class);
                intent1.putExtra("item",group);
                startActivity(intent1);
                break;
            case R.id.go_gruop:
                Intent intent2 = new Intent(GroupDetailActivity.this, ArticleListActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.member_state:
                join();
                break;
            default:
                break;
        }
    }

    private void join() {
        if(group.isIn()){
            exitGroup();
        }else{
            addGroup();
        }
    }


    /**
     * 申请加入社区
     */
    public void addGroup() {
        String url = "http://jinronghui.wang:8080/publics/group/join.do";
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
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        handler.sendEmptyMessage(0);
                        GlobalFunction.showToast(GroupDetailActivity.this,"加群成功。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(GroupDetailActivity.this,"操作失败，请稍后重试...");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(GroupDetailActivity.this,"操作失败，请稍后重试...");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 退出社区
     */
    public void exitGroup() {
        String url = "http://jinronghui.wang:8080/publics/group/exit.do";
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
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        handler.sendEmptyMessage(0);
                        GlobalFunction.showToast(GroupDetailActivity.this,"退群成功。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(GroupDetailActivity.this,"操作失败，请稍后重试...");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(GroupDetailActivity.this,"操作失败，请稍后重试...");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 成员列表
     */
    public void getGroupMembers() {
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
                        gms.add(gm);
                    }
                    handler.sendEmptyMessage(1);
                    getApplyMembers();
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(GroupDetailActivity.this,"操作失败，请稍后重试...");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(GroupDetailActivity.this,"操作失败，请稍后重试...");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 成员列表
     */
    public void getApplyMembers() {
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
                    handler.sendEmptyMessage(2);
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(GroupDetailActivity.this,"操作失败，请稍后重试...");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(GroupDetailActivity.this,"操作失败，请稍后重试...");
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }


    private void fetchData() {
        getGroupMembers();
    }
}
