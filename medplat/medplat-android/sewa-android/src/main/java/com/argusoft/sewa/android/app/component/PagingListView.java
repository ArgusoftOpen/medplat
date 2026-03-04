package com.argusoft.sewa.android.app.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.argusoft.sewa.android.app.R;
import com.argusoft.sewa.android.app.databean.ListItemDataBean;

import java.util.List;

/**
 * Created by prateek on 11/29/19
 */
public class PagingListView extends ListView {

    private boolean isLoading;
    private boolean hasMoreItems;
    private PagingListener pagingListener;
    private LoadingView loadingView;
    private OnScrollListener onScrollListener;

    public PagingListView(Context context) {
        super(context);
        init();
    }

    public PagingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PagingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setPagingListener(PagingListener pagingListener) {
        this.pagingListener = pagingListener;
    }

    public void setHasMoreItems(boolean hasMoreItems) {
        this.hasMoreItems = hasMoreItems;
        if (!this.hasMoreItems) {
            removeFooterView(loadingView);
        } else if (findViewById(R.id.loading_view) == null) {
            addFooterView(loadingView);
            ListAdapter adapter = ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
            setAdapter(adapter);
        }
    }

    public void onFinishLoadingWithItem(boolean hasMoreItems, List<ListItemDataBean> newItems) {
        setHasMoreItems(hasMoreItems);
        setIsLoading(false);
        if (newItems != null && !newItems.isEmpty()) {
            ListAdapter adapter = ((HeaderViewListAdapter) getAdapter()).getWrappedAdapter();
            if (adapter instanceof PagingAdapter) {
                ((PagingAdapter) adapter).addMoreItems(newItems);
            }
        }
    }

    private void init() {
        isLoading = false;
        loadingView = new LoadingView(getContext());
        addFooterView(loadingView);
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Dispatch to child OnScrollListener
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //Dispatch to child OnScrollListener
                if (onScrollListener != null) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (!isLoading && hasMoreItems && pagingListener != null && (lastVisibleItem == totalItemCount)) {
                    isLoading = true;
                    pagingListener.onLoadMoreItems();

                }
            }
        });
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }

    public interface PagingListener {
        void onLoadMoreItems();
    }
}
