package org.kudrenko.telegram.ui;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.TelegramApplication;
import org.kudrenko.telegram.api.Errors;

@EFragment
public abstract class AbsTelegramFragment extends Fragment {
    @App
    protected TelegramApplication application;

    @UiThread
    protected void showError(TdApi.Error error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.error).setMessage(Errors.find(error.text));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", getClass().getName());
    }

    public void send(TdApi.TLFunction function, Client.ResultHandler handler) {
        application.send(function, handler);
    }
}
