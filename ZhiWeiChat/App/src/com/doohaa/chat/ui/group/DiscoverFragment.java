package com.doohaa.chat.ui.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.doohaa.chat.R;
import com.doohaa.chat.adapter.MyAdapter;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.model.Image;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.article.ArticleListActivity;
import com.doohaa.chat.ui.common.BaseFragment;
import com.doohaa.chat.ui.view.LoadMoreListView;
import com.doohaa.chat.utils.GlobalFunction;
import com.doohaa.chat.utils.ImageLoaderHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends BaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView listView;
    private MyAdapter adapter;
    private ImageLoaderHelper imageLoaderHelper;
    private View rootView;
    private List<Group> groups;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.em_fragment_discover, container, false);
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
        groups = new ArrayList<>();
        adapter = new MyAdapter(getActivity(),groups);
        imageLoaderHelper = new ImageLoaderHelper(getActivity());
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        listView = (LoadMoreListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        swipeLayout.setOnRefreshListener(this);
        listView.setEnableLoadMore(true);
        listView.setOnLoadMoreListener(onLoadMoreListener);
        swipeLayout.setOnRefreshListener(this);

    }

    LoadMoreListView.OnLoadMoreListener onLoadMoreListener = new LoadMoreListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            //loadMoreCompleted();
//            startNum = groups.size();
//            fetchData();
            handler.sendEmptyMessage(0);
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
        String url = "http://jinronghui.wang:8080/publics/group/query.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(getActivity()).getToken();
        params.addBodyParameter("token",token);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //����result
                try {
                    JSONObject jsObj = new JSONObject(result);
                    String data = jsObj.optString("data");
                    JSONArray jsAry = new JSONArray(data);
                    Gson gson = new Gson();
                    for(int i=0;i<jsAry.length();i++){
                        String obj = jsAry.getString(i);
                        JSONObject jObj = new JSONObject(obj);
                        Group group = new Group();
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
                        group.setIn(jObj.optBoolean("isIn"));
                        group.setImage(image);
                        groups.add(group);
                    }
                    adapter.notifyDataSetChanged();
                    System.out.println("dffgn");
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ArticleListActivity.class);
        intent.putExtra("item", groups.get(position));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        groups.clear();
        adapter.notifyDataSetChanged();
        fetchData();
    }

}
