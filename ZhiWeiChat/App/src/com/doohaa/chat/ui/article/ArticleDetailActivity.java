package com.doohaa.chat.ui.article;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.doohaa.chat.R;
import com.doohaa.chat.model.Article;
import com.doohaa.chat.model.Comment;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.model.Image;
import com.doohaa.chat.model.Note;
import com.doohaa.chat.model.NoteMessage;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.utils.DividerItemDecoration;
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

public class ArticleDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LayoutInflater layoutInflater;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout materialRefreshLayout;
    private EditText etContent;
    private TextView tvSend;
    private LinearLayout llContent;
    private ArticleDetailAdapter1 adapter;
    private ImageLoaderHelper imageLoaderHelper;
    private ArrayList<Note> datas;
    private ArticleDetailHeader headerRootView;
    private EaseTitleBar easeTitleBar;
    private Article article;
    private int numPerPage = 10;
    private int startNum = 0;
    private Note nm;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_article_detail);
        article = (Article) getIntent().getSerializableExtra("item");
        group = (Group) getIntent().getSerializableExtra("group");
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        datas = new ArrayList<>();
        headerRootView = new ArticleDetailHeader(this);
        headerRootView.setData(article);
        headerRootView.setPraise(clk1);
        headerRootView.setUp(clk2);
        headerRootView.setDel(clk3);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new ArticleDetailAdapter1(this, datas);
        easeTitleBar = (EaseTitleBar) findViewById(R.id.title_bar);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh_layout);
        tvSend = (TextView) findViewById(R.id.submit);
        etContent = (EditText) findViewById(R.id.edt);
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
                getAnswers(article.getId());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        llContent.addView(headerRootView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void registEvent() {
        easeTitleBar.setLeftLayoutClickListener(this);
        tvSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.submit:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        String content = etContent.getText().toString();
        if(TextUtils.isEmpty(content)){
            GlobalFunction.showToast(this,"评论内容不能为空！");
            etContent.requestFocus();
            return;
        }
        if(nm != null){
            answerComment(content,nm.getId());
        }else{
            answerArticle(content);
        }
    }

    private void fetchData() {
//        getComments();
        getAnswers(article.getId());
    }

    /**
     * 回复主题
     */
    private void getAnswers(int id) {
        String url = "http://jinronghui.wang:8080/publics/note/replay/query.do";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("noteId", id + "");
        params.addBodyParameter("numPerPage", numPerPage + "");
        params.addBodyParameter("startNum", startNum + "");
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token", token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<NoteMessage>>() {
                    }.getType();
                    JSONObject jsObj = new JSONObject(result);
                    String data = jsObj.optString("data");
                    JSONArray jsAry = new JSONArray(data);
                    for(int i=0;i<jsAry.length();i++) {
                        String obj = jsAry.getString(i);
                        JSONObject jObj = new JSONObject(obj);
                        Note note = new Note();
                        note.setId(jObj.optInt("id"));
                        note.setFkNoteId(jObj.optInt("fkNoteId"));
                        note.setMessage(jObj.optString("message"));
                        note.setFkGroupId(jObj.optInt("fkGroupId"));
                        note.setFkMemberId(jObj.optInt("fkMemberId"));
                        note.setType(jObj.optInt("type"));
                        note.setCreateTime(jObj.optLong("createTime"));
                        note.setOwner(jObj.optString("owner"));
                        note.setUpCount(jObj.optInt("upCount"));
                        note.setIsUp(jObj.optBoolean("isUp"));
                        String img = jObj.optString("memberImg");
                        Image image = gson.fromJson(img, Image.class);
                        note.setImage(image);
                        String notes = jObj.optString("noteMessageList");
                        List<NoteMessage> nmsgs = gson.fromJson(notes, type);
                        note.setNotes(nmsgs);
                        datas.add(note);
                    }
                    adapter.notifyDataSetChanged();
                    handler.sendEmptyMessage(2);
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(ArticleDetailActivity.this, R.string.get_data_failure);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticleDetailActivity.this, R.string.get_data_failure);
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
     * 回复主题
     */
    private void answerArticle(String content) {
        String url = "http://jinronghui.wang:8080/publics/note/replay.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token", token);
        params.addBodyParameter("message", content);
        params.addBodyParameter("fkGroupId", article.getFkGroupId() + "");
        params.addBodyParameter("fkMemberId", article.getFkMemberId() + "");
        params.addBodyParameter("imageIdList", "");
        params.addBodyParameter("fkNoteId", article.getId() + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        String data = jsObj.optString("data");
                        JSONObject jObj = new JSONObject(data);
                        Note note = new Note();
                        note.setId(jObj.optInt("id"));
                        note.setFkNoteId(jObj.optInt("fkNoteId"));
                        note.setMessage(jObj.optString("message"));
                        note.setFkGroupId(jObj.optInt("fkGroupId"));
                        note.setFkMemberId(jObj.optInt("fkMemberId"));
                        note.setType(jObj.optInt("type"));
                        note.setCreateTime(jObj.optLong("createTime"));
                        note.setOwner(jObj.optString("owner"));
                        note.setUpCount(jObj.optInt("upCount"));
                        note.setIsUp(jObj.optBoolean("isUp"));
                        String img = jObj.optString("memberImg");
                        Image image = gson.fromJson(img, Image.class);
                        note.setImage(image);
                        String notes = jObj.optString("noteMessageList");
                        Type type = new TypeToken<List<NoteMessage>>() {
                        }.getType();
                        List<NoteMessage> nmsgs = gson.fromJson(notes, type);
                        note.setNotes(nmsgs);
                        datas.add(note);
                        adapter.notifyDataSetChanged();
                        handler.sendEmptyMessage(3);
                        GlobalFunction.showToast(ArticleDetailActivity.this,"回复成功。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(ArticleDetailActivity.this,"回复失败，请稍后重试...");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticleDetailActivity.this,"回复失败，请稍后重试...");
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
     * 回复评论
     */
    private void answerComment(String content, int commentId) {
        String url = "http://jinronghui.wang:8080/publics/note/message/replay.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token", token);
        params.addBodyParameter("fkNoteId", commentId + "");
        params.addBodyParameter("message", content);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        String data = jsObj.optString("data");
                        Gson gson = new Gson();
                        NoteMessage note = gson.fromJson(data,NoteMessage.class);
                        List<NoteMessage> nms = nm.getNotes();
                        if(nms == null){
                            nms = new ArrayList<NoteMessage>();
                        }
                        nms.add(note);
                        nm.setNotes(nms);
                        adapter.notifyDataSetChanged();
                        GlobalFunction.showToast(ArticleDetailActivity.this,"回复成功。");
                        nm = null;
                        handler.sendEmptyMessage(3);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(ArticleDetailActivity.this,"回复失败，请稍后重试...");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticleDetailActivity.this,"回复失败，请稍后重试...");
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
     * 删除一级评论
     */
    public void deleteReplay(final Note nm) {
        String url = "http://jinronghui.wang:8080/publics/note/message/replay.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token", token);
        params.addBodyParameter("messageId", nm.getId() + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        datas.remove(nm);
                        adapter.notifyDataSetChanged();
                        GlobalFunction.showToast(ArticleDetailActivity.this,"删除回复成功。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(ArticleDetailActivity.this,"删除回复失败，请稍后重试...");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticleDetailActivity.this,"删除回复失败，请稍后重试...");
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
     * 置顶
     */
    public void up(final boolean up) {
        String url = "http://jinronghui.wang:8080/publics/note/top.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token", token);
        params.addBodyParameter("groupId", group.getId() + "");
        params.addBodyParameter("noteId", article.getId() + "");
        params.addBodyParameter("flag", !up + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        if(up){
                            GlobalFunction.showToast(ArticleDetailActivity.this,"取消置顶成功。");
                        }else{
                            GlobalFunction.showToast(ArticleDetailActivity.this,"置顶成功。");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(up){
                        GlobalFunction.showToast(ArticleDetailActivity.this,"取消置顶失败，请稍后重试...");
                    }else{
                        GlobalFunction.showToast(ArticleDetailActivity.this,"置顶失败，请稍后重试...");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(up){
                    GlobalFunction.showToast(ArticleDetailActivity.this,"取消置顶失败，请稍后重试...");
                }else{
                    GlobalFunction.showToast(ArticleDetailActivity.this,"置顶失败，请稍后重试...");
                }
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
     * 删除二级评论
     */
    public void deleteComment(final Note nm,final int commentId) {
        String url = "http://jinronghui.wang:8080/publics/note/message/replay.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token", token);
        params.addBodyParameter("messageId", commentId + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        String data = jsObj.optString("data");
                        List<NoteMessage> nms = nm.getNotes();
                        if(nms != null){
                            for(int i=0;i<nms.size();i++){
                                NoteMessage nm = nms.get(i);
                                if(nm.getId() == commentId){
                                    nms.remove(i);
                                    break;
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        GlobalFunction.showToast(ArticleDetailActivity.this,"删除回复成功。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(ArticleDetailActivity.this,"删除回复失败，请稍后重试...");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticleDetailActivity.this,"删除回复失败，请稍后重试...");
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
                    headerRootView.setPraiseCount(article.isUp());
                    break;
                case 1:
                    materialRefreshLayout.finishRefresh();
                    break;
                case 2:
                    materialRefreshLayout.finishRefreshLoadMore();
                    break;
                case 3:
                    etContent.setText("");
                    etContent.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 点赞
     */
    private void praise(boolean praise) {
        String url = "http://jinronghui.wang:8080/publics/note/up.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token", token);
        params.addBodyParameter("noteId", article.getId() + "");
        params.addBodyParameter("up", praise+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String code = jsObj.optString("code");
                    if(TextUtils.equals(code,"00")){
                        if(article.isUp()){
                            GlobalFunction.showToast(ArticleDetailActivity.this,"取消点赞成功。");
                        }else{
                            GlobalFunction.showToast(ArticleDetailActivity.this,"点赞成功。");
                        }
                    }
                    article.setUp(!article.isUp());
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    if(article.isUp()){
                        GlobalFunction.showToast(ArticleDetailActivity.this,"取消点赞失败，请稍后重试。");
                    }else{
                        GlobalFunction.showToast(ArticleDetailActivity.this,"点赞失败，请稍后重试。");
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if(article.isUp()){
                    GlobalFunction.showToast(ArticleDetailActivity.this,"取消点赞失败，请稍后重试。");
                }else{
                    GlobalFunction.showToast(ArticleDetailActivity.this,"点赞失败，请稍后重试。");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    View.OnClickListener clk1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            praise(article.isUp());
        }
    };

    View.OnClickListener clk2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            up(true);
        }
    };

    View.OnClickListener clk3 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deletePost(article);
        }
    };

    /**
     * 删除帖子
     */
    public void deletePost(final Article article) {
        String url = "http://jinronghui.wang:8080/publics/note/delete.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
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
                        GlobalFunction.showToast(ArticleDetailActivity.this,"删除成功。");
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(ArticleDetailActivity.this,"删除失败，请稍后重试...");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(ArticleDetailActivity.this,"删除失败，请稍后重试...");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public void answer(Note nm){
        this.nm = nm;
        etContent.setFocusable(true);
        etContent.setFocusableInTouchMode(true);
        etContent.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)etContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etContent, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
