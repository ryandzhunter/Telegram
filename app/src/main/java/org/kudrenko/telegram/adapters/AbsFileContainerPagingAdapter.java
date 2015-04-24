package org.kudrenko.telegram.adapters;

import android.content.Context;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.List;

public abstract class AbsFileContainerPagingAdapter<Item, Holder> extends AbsPagingAdapter<Item, Holder> {

    public AbsFileContainerPagingAdapter(Context mContext) {
        super(mContext);
    }

    public AbsFileContainerPagingAdapter(List<Item> items, Context mContext) {
        super(items, mContext);
    }

    public abstract void updateFile(TdApi.FileLocal file);
}
