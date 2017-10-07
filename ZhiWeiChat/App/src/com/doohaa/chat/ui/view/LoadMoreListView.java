package com.doohaa.chat.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.ListView;

import com.doohaa.chat.R;

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = "LoadMoreListView";

    private View footerView;
    private View footerContentView;

    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoadingMore = false;

    private OnScrollListener onScrollListener;
    private int lastScrollState;

    private boolean enableLoadMore = false;

    public LoadMoreListView(Context context) {
        super(context);

        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context) {
        super.setOnScrollListener(this);

        setSelector(android.R.color.transparent);
        setDividerHeight(0);
        footerView = LayoutInflater.from(context).inflate(R.layout.load_more_footer_layout, this, false);
        addFooterView(footerView);
    }

    public void setLoadMoreLayout(Context context, int resourceId) {
        ViewStub stub = (ViewStub) footerView.findViewById(R.id.load_more_stub);
        if (resourceId > 0) {
            stub.setLayoutResource(resourceId);
        }
        stub.setVisibility(View.VISIBLE);

        footerContentView = footerView.findViewById(R.id.load_more_layout);
    }

    private void loadMore() {
        isLoadingMore = true;

        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
    }

    public void loadMoreCompleted() {
        isLoadingMore = false;
        hideLoadMoreLayout();
    }

    public void showLoadMoreLayout() {
        if (footerContentView != null) {
            footerContentView.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoadMoreLayout() {
        if (footerContentView != null) {
            footerContentView.setVisibility(View.GONE);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.lastScrollState = scrollState;

        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (onLoadMoreListener != null) {
            if (visibleItemCount == totalItemCount) {
                if (footerContentView != null) {
                    footerContentView.setVisibility(View.GONE);
                }
                return;
            }

            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
            if (loadMore && isLoadingMore == false && enableLoadMore /*&& lastScrollState != SCROLL_STATE_IDLE*/) {
                loadMore();
            }
        }
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public boolean isEnableLoadMore() {
        return enableLoadMore;
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
    }

    public View getFooterContentView() {
        return footerContentView;
    }
}
