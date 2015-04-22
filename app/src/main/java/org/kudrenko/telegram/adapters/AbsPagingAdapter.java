package org.kudrenko.telegram.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paging.listview.PagingBaseAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public abstract class AbsPagingAdapter<Item, Holder> extends PagingBaseAdapter<Item> {
    protected Context mContext;

    public AbsPagingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public AbsPagingAdapter(List<Item> items, Context mContext) {
        super(items);
        this.mContext = mContext;
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

    protected void displayImage(String url, ImageView imageView) {
        Picasso.with(mContext).load(url).into(imageView);
    }

    protected Item getItem_(int position) {
        return (Item) getItem(position);
    }

    protected abstract void bindView(Holder holder, Item item_);

    protected abstract void bindHolder(Holder holder, View convertView);

    protected abstract int layout();

    protected abstract Holder createHolder();
}
