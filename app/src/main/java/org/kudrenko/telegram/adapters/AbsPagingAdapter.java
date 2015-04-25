package org.kudrenko.telegram.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paging.listview.PagingBaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.List;

@EBean
public abstract class AbsPagingAdapter<Item, Holder> extends PagingBaseAdapter<Item> {
    @RootContext
    protected Context mContext;

    public AbsPagingAdapter() {
    }

    public AbsPagingAdapter(List<Item> items) {
        super(items);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        Item item_ = getItem_(position);
        if (convertView == null) {
            holder = createHolder();
            convertView = View.inflate(mContext, layout(), null);

            bindHolder(holder, convertView);

            convertView.setTag(holder);
        } else holder = (Holder) convertView.getTag();

        bindView(holder, item_);

        return convertView;
    }

    protected Item getItem_(int position) {
        return (Item) getItem(position);
    }

    protected abstract void displayImage(TdApi.File file, ImageView imageView);

    protected abstract void bindView(Holder holder, Item item_);

    protected abstract void bindHolder(Holder holder, View convertView);

    protected abstract int layout();

    protected abstract Holder createHolder();
}
