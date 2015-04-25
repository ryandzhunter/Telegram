package org.kudrenko.telegram.adapters;

import android.widget.ImageView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.TelegramApplication;
import org.kudrenko.telegram.components.RoundedTransformation;
import org.kudrenko.telegram.otto.OttoBus;
import org.kudrenko.telegram.otto.events.UpdateFileEvent;

import java.io.File;
import java.util.List;

@EBean
public abstract class AbsFileContainerPagingAdapter<Item, Holder> extends AbsPagingAdapter<Item, Holder> {
    @App
    TelegramApplication application;

    @Bean
    OttoBus ottoBus;

    public AbsFileContainerPagingAdapter() {
        super();
    }

    public AbsFileContainerPagingAdapter(List<Item> items) {
        super(items);
    }

    @AfterInject
    void afterInject() {
        ottoBus.register(this);
    }

    public abstract void updateFile(TdApi.FileLocal file);

    @Override
    public void displayImage(final TdApi.File file, final ImageView imageView) {
        if (file.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR) {
            int id = ((TdApi.FileEmpty) file).id;
            if (id != 0) {
                application.send(new TdApi.DownloadFile(id));
            }
        } else if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
            show((TdApi.FileLocal) file, imageView);
        }
    }

    private void show(TdApi.FileLocal file, ImageView imageView) {
        Picasso.with(mContext).load(new File(file.path)).transform(new RoundedTransformation()).into(imageView);
    }

    @Subscribe
    public void onFileUpdate(UpdateFileEvent event) {
        TdApi.FileLocal file = event.file;
        updateFile(file);
        notifyDataSetChanged();
    }
}
