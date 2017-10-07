package com.doohaa.chat.ui.article;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.doohaa.chat.R;
import com.doohaa.chat.model.Article;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.model.Image;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.group.GroupDetailActivity;
import com.doohaa.chat.ui.view.LoadMoreListView;
import com.doohaa.chat.utils.GlobalFunction;
import com.doohaa.chat.utils.ImageLoaderHelper;
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

public class ArticleListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView listView;
    private ArticleListAdapter adapter;
    private ImageLoaderHelper imageLoaderHelper;
    private ArrayList<Article> articles;
    private ArticleListHeader headerRootView;
    private EaseTitleBar easeTitleBar;
    private View headerView;
    private Button btnPost;
    private Group group;
    private int startNum = 0;
    private int numPerPage = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_article_list);
        initComponent();
        registEvent();
        getGroupDetails();
        //fetchData();
    }

    private void initComponent() {
        group = (Group)getIntent().getSerializableExtra("item");
        articles = new ArrayList<>();
        headerRootView = new ArticleListHeader(this);
        headerRootView.setData(group);
        headerView = headerRootView.findViewById(R.id.header_root_view);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        adapter = new ArticleListAdapter(this, articles);
        adapter.setGroup(group);
        adapter.setAac(this);
        easeTitleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        listView = (LoadMoreListView) findViewById(R.id.list);
        listView.setEnableLoadMore(true);
        btnPost = (Button) findViewById(R.id.post);
        listView.setAdapter(adapter);
        listView.addHeaderView(headerRootView);
    }

    private void registEvent() {
        easeTitleBar.setLeftLayoutClickListener(this);
        headerView.setOnClickListener(this);
        swipeLayout.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        listView.setOnLoadMoreListener(onLoadMoreListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.header_root_view:
                Intent intent = new Intent(ArticleListActivity.this, GroupDetailActivity.class);
                intent.putExtra("item",group);
                startActivity(intent);
                break;
            case R.id.post:
                Intent intent1 = new Intent(ArticleListActivity.this, ArticlePostActivity.class);
                intent1.putExtra("item",group);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    LoadMoreListView.OnLoadMoreListener onLoadMoreListener = new LoadMoreListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            loadMoreCompleted();
        }
    };

    private void loadMoreCompleted() {
        if (listView != null) {
            listView.loadMoreCompleted();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    headerRootView.setData(group);
                    fetchData();
                    break;
                case 1:
                    loadMoreCompleted();
                    swipeLayout.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };


    private void fetchData() {
        String url = "http://jinronghui.wang:8080/publics/note/query.do";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("groupId",group.getId()+"");
        params.addBodyParameter("startNum",startNum+"");
        params.addBodyParameter("numPerPage",numPerPage+"");
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
                    Type type = new TypeToken<List<Image>>() {}.getType();
                    for(int i=0;i<jsAry.length();i++){
                        String obj = jsAry.getString(i);
                        JSONObject jObj = new JSONObject(obj);
                        Article article = new Article();
                        article.setId(jObj.optInt("id"));
                        article.setFkNoteId(jObj.optInt("fkNoteId"));
                        article.setMessage(jObj.optString("message"));
                        article.setFkGroupId(jObj.optInt("fkGroupId"));
                        article.setFkMemberId(jObj.optInt("fkMemberId"));
                        article.setType(jObj.optInt("type"));
                        article.setCreateTime(jObj.optLong("createTime"));
                        article.setOwner(jObj.optString("owner"));
                        article.setUpCount(jObj.optInt("upCount"));
                        article.setIsUp(jObj.optBoolean("isUp"));
                        article.setTop(jObj.optBoolean("isTop"));
                        String img = jObj.optString("imageList");
                        List<Image> images = gson.fromJson(img,type);
                        String member = jObj.optString("memberImg");
                        JSONObject mObj = new JSONObject(member);
                        String url = mObj.optString("url");
                        article.setAvatar(url);
                        article.setImageList(images);
                        articles.add(article);
                    }
                    adapter.notifyDataSetChanged();
                    System.out.println("dffgn");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticleListActivity.this,R.string.get_data_failure);
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    private void getGroupDetails() {
        String url = "http://jinronghui.wang:8080/publics/group/query.do";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("id",group.getId()+"");
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
                    for(int i=0;i<jsAry.length();i++){
                        String obj = jsAry.getString(i);
                        JSONObject jObj = new JSONObject(obj);
                        group = new Group();
                        group.setId(jObj.optInt("id"));
                        group.setName(jObj.optString("name"));
                        group.setCertification(jObj.optString("certification"));
                        group.setDescription(jObj.optString("description"));
                        group.setOwnerPhone(jObj.optString("ownerPhone"));
                        group.setMemberCount(jObj.optInt("memberCount"));
                        group.setNoteCount(jObj.optInt("noteCount"));
                        group.setCreateTime(jObj.optString("createTime"));
                        group.setState(jObj.optInt("state"));
                        String img = jObj.optString("imgProperty");
                        Image image = gson.fromJson(img,Image.class);
                        group.setImage(image);
                        handler.sendEmptyMessage(0);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticleListActivity.this,R.string.get_data_failure);
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
     * 删除帖子
     */
    public void deletePost(final Article article) {
        String url = "http://jinronghui.wang:8080/publics/note/delete.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(ArticleListActivity.this).getToken();
        params.addBodyParameter("token", token);
        params.addBodyParameter("noteId", article.getId() + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        articles.remove(article);
                        adapter.notifyDataSetChanged();
                        GlobalFunction.showToast(ArticleListActivity.this,"删除成功。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(ArticleListActivity.this,"删除失败，请稍后重试...");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticleListActivity.this,"删除失败，请稍后重试...");
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
        articles.clear();
        adapter.notifyDataSetChanged();
        startNum = 0;
        fetchData();
    }
}
