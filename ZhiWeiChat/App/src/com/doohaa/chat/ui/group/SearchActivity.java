package com.doohaa.chat.ui.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doohaa.chat.R;
import com.doohaa.chat.model.Group;
import com.doohaa.chat.model.Image;
import com.doohaa.chat.preferences.TokenPreferences;
import com.doohaa.chat.ui.article.ArticleListActivity;
import com.doohaa.chat.ui.common.BaseActivity;
import com.doohaa.chat.ui.view.LoadMoreListView;
import com.doohaa.chat.utils.GlobalFunction;
import com.doohaa.chat.utils.ImageLoaderHelper;
import com.google.gson.Gson;
import com.lling.photopicker.utils.ImageManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreListView listView;
    private EditText etKeyWord;
    private MyAdapter adapter;
    private ImageLoaderHelper imageLoaderHelper;
//    private ArrayList<Search> searches;
    private RelativeLayout rltyLeft;
    private ImageView ivSearch;
    private List<Group> groups = new ArrayList<>();
    private int numPerPage = 10;
    private int startNum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_search);
        initComponent();
        registEvent();
        //fetchData();
    }

    private void initComponent() {
//        searches = new ArrayList<>();
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new MyAdapter(groups);
        listView = (LoadMoreListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        etKeyWord = (EditText) findViewById(R.id.edt_keyword);
        rltyLeft = (RelativeLayout) findViewById(R.id.left_layout);
        ivSearch = (ImageView) findViewById(R.id.right_image);
        listView.setAdapter(adapter);
        listView.setEnableLoadMore(true);
        listView.setOnLoadMoreListener(onLoadMoreListener);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    LoadMoreListView.OnLoadMoreListener onLoadMoreListener = new LoadMoreListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            //loadMoreCompleted();
            startNum = groups.size();
            fetchData();
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
                    loadMoreCompleted();
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        };
    };

    private void registEvent() {
        rltyLeft.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.right_image:
                search();
                break;
            default:
                break;
        }
    }

    private void search() {
        String url = "http://jinronghui.wang:8080/publics/group/query.do";
        RequestParams params = new RequestParams(url);
        String token = TokenPreferences.getInstance(this).getToken();
        params.addBodyParameter("token",token);
//        params.addBodyParameter("ownerPhone","");
//        params.addBodyParameter("id","");
        params.addBodyParameter("name",etKeyWord.getText().toString());
//        params.addBodyParameter("certification","");
//        params.addBodyParameter("description","");
        params.addBodyParameter("numPerPage",numPerPage+"");
        params.addBodyParameter("startNum",startNum+"");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //解析result
                try {
                    JSONArray jsAry = new JSONArray(result);
                    Gson gson = new Gson();
                    groups.clear();
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
                GlobalFunction.showToast(SearchActivity.this,R.string.get_data_failure);
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

    private void fetchData() {
        search();
    }

    @Override
    public void onRefresh() {
        groups.clear();
        adapter.notifyDataSetChanged();
        startNum = 0;
        fetchData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ArticleListActivity.class);
//        intent.putExtra("id", topics.get(position).getId());
        startActivity(intent);
    }

    private class MyAdapter extends BaseAdapter {
        private ImageManager manager = new ImageManager();
        private List<Group> groups;
        public MyAdapter(List<Group> groups){
            this.groups = groups;
        }
        @Override
        public int getCount() {
            return groups.size();
        }

        @Override
        public Object getItem(int position) {
            return groups.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            Group group = groups.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.item_search, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.tvName = (TextView)convertView.findViewById(R.id.group_name);
                holder.tvAuth = (TextView)convertView.findViewById(R.id.auth);
                holder.tvDesc = (TextView)convertView.findViewById(R.id.des);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            manager.displayImage(holder.img,group.getImage().getUrl(), ImageManager.ShowType.URL);
            holder.tvName.setText(group.getName());
            holder.tvAuth.setText(group.getCertification());
            holder.tvDesc.setText(group.getDescription());
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView img;
        TextView tvName;
        TextView tvAuth;
        TextView tvDesc;
    }
}
