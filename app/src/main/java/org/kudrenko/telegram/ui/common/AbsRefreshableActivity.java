package org.kudrenko.telegram.ui.common;

import android.support.v4.widget.SwipeRefreshLayout;

import com.paging.listview.PagingListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.adapters.AbsPagingAdapter;

import java.util.Arrays;

@EActivity
public abstract class AbsRefreshableActivity<Item, Holder, Adapter extends AbsPagingAdapter<Item, Holder>> extends AbsTelegramActivity
        implements SwipeRefreshLayout.OnRefreshListener, PagingListView.Pagingable {
    public static final int LIMIT = 100;

    @ViewById(android.R.id.list)
    protected PagingListView listView;

    @ViewById(R.id.refresh)
    protected SwipeRefreshLayout refreshLayout;

    protected Adapter adapter;

    @AfterViews
    protected void afterViews() {
        initSwipeLayout();
        initListView();

        onRefresh();
    }

    private void initSwipeLayout() {
        refreshLayout.setOnRefreshListener(this);
    }

    private void initListView() {
        adapter = createAdapter();
        listView.setAdapter(adapter);

        listView.setHasMoreItems(false);
        listView.setPagingableListener(this);
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        update(true);
    }

    @Override
    public void onLoadMoreItems() {
        update(false);
    }

    @UiThread
    protected void setData(Item[] items, boolean reload) {
        if (reload)
            adapter.removeAllItems();
        listView.onFinishLoading(items.length >= LIMIT, Arrays.asList(items));
    }

    protected abstract Adapter createAdapter();

    protected abstract void update(boolean reload);
}
