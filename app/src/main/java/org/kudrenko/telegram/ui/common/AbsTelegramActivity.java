package org.kudrenko.telegram.ui.common;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.TelegramApplication;
import org.kudrenko.telegram.api.Errors;
import org.kudrenko.telegram.otto.OttoBus;
import org.kudrenko.telegram.ui.AbsTelegramFragment;

@EActivity
public abstract class AbsTelegramActivity extends ActionBarActivity {
    protected final String TAG = getClass().getName();

    @App
    protected TelegramApplication application;

    @Bean
    protected OttoBus ottoBus;

    @Override
    protected void onStart() {
        ottoBus.register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ottoBus.unregister(this);
    }

    public void send(TdApi.TLFunction function, Client.ResultHandler handler) {
        application.send(function, handler);
    }

    public void send(TdApi.TLFunction function) {
        application.send(function, null);
    }

    protected void openFragment(AbsTelegramFragment fragment, boolean withSlidingAnimation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (withSlidingAnimation) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public Cursor countries() {
        return application.helper.countries();
    }

    @UiThread
    protected void showError(TdApi.Error error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error).setMessage(localizeError(error));
        builder.show();
    }

    protected void onError(TdApi.Error error) {
        showError(error);
    }

    protected Client.ResultHandler resultHandler(final Client.ResultHandler handler) {
        return new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                if (object.getConstructor() == TdApi.Error.CONSTRUCTOR) {
                    onError((TdApi.Error) object);
                } else if (handler != null) {
                    handler.onResult(object);
                }
            }
        };
    }

    protected Client.ResultHandler resultHandler() {
        return resultHandler(null);
    }

    public int localizeError(TdApi.Error error) {
        return Errors.find(error.text);
    }

    public void displayImage(final TdApi.File file, final ImageView imageView) {
        if (file.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR) {
            int id = ((TdApi.FileEmpty) file).id;
            if (id != 0) {
                send(new TdApi.DownloadFile(id), new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.TLObject object) {
                        if (object.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                            show((TdApi.FileLocal) object, imageView);
                        }
                    }
                });
            }
        } else if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
            show((TdApi.FileLocal) file, imageView);
        }
    }

    private void show(TdApi.FileLocal file, ImageView imageView) {
        Picasso.with(this).load(file.path).into(imageView);
    }
}
