package com.doohaa.chat.ui.group;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import com.doohaa.chat.R;
import com.doohaa.chat.model.Article;
import com.doohaa.chat.model.Image;
import com.doohaa.chat.model.GroupMember;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.article.ArticleListAdapter;
import com.doohaa.chat.ui.common.BaseFragment;
import com.doohaa.chat.ui.view.LoadMoreListView;
import com.doohaa.chat.ui.wallet.BulletinActivity;
import com.doohaa.chat.utils.GlobalFunction;
import com.doohaa.chat.utils.ImageLoaderHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class TopicFragment extends BaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static TopicFragment newInstance() {
        return new TopicFragment();
    }
    private int startNum = 0;
    private int numPerPage = 10;
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView listView;
    private  ArticleListAdapter adapter;;
    private ImageLoaderHelper imageLoaderHelper;
    private View rootView;
    private ArrayList<Article> articles;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.em_fragment_topic, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initComponent();
        registEvent();
        fetchData();
    }

    private void initComponent() {
        articles = new ArrayList<>();
        adapter = new ArticleListAdapter(getActivity(), articles);
        adapter.setFrag(this);
        imageLoaderHelper = new ImageLoaderHelper(getActivity());
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        listView = (LoadMoreListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setEnableLoadMore(true);
        listView.setOnLoadMoreListener(onLoadMoreListener);
        swipeLayout.setOnRefreshListener(this);

    }

    LoadMoreListView.OnLoadMoreListener onLoadMoreListener = new LoadMoreListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            //loadMoreCompleted();
            startNum = articles.size();
            fetchData();
        }
    };

    private void loadMoreCompleted() {
        if (listView != null) {
            listView.loadMoreCompleted();
        }
    }

    private void registEvent() {
        listView.setOnItemClickListener(this);
    }

    private void fetchData() {
        String url = "http://jinronghui.wang:8080/publics/group/notes.do";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("startNum",startNum+"");
        params.addBodyParameter("numPerPage",numPerPage+"");
        String token = TokenPreferences.getInstance(getActivity()).getToken();
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
                        article.setFkGroupName(jObj.optString("fkGroupName"));
                        article.setUpCount(jObj.optInt("upCount"));
                        article.setIsUp(jObj.optBoolean("isUp"));
                        article.setTop(jObj.optBoolean("isTop"));
                        String member = jObj.optString("memberImg");
                        GroupMember m = gson.fromJson(member,GroupMember.class);
                        article.setAvatar(m.getUrl());
                        String img = jObj.optString("imageList");
                        List<Image> images = gson.fromJson(img,type);
                        article.setImageList(images);
                        articles.add(article);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(getActivity(),R.string.get_data_failure);
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

    /**
     * 删除帖子
     */
    public void deletePost(final Article article) {
        String url = "http://jinronghui.wang:8080/publics/note/delete.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(getActivity()).getToken();
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
                        GlobalFunction.showToast(getActivity(),"删除成功。");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    GlobalFunction.showToast(getActivity(),"删除失败，请稍后重试...");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                GlobalFunction.showToast(getActivity(),"删除失败，请稍后重试...");
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), BulletinActivity.class);
        intent.putExtra("item", articles.get(position));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        articles.clear();
        adapter.notifyDataSetChanged();
        startNum = 0;
        fetchData();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
//            return bulletins.size();
            return 10;
        }

        @Override
        public Object getItem(int position) {
//            return bulletins.get(position);
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
                convertView = layoutInflater.inflate(R.layout.item_topic, null);
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
