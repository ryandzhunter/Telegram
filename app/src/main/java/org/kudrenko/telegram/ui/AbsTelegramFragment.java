package org.kudrenko.telegram.ui;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.TelegramApplication;

@EFragment
public abstract class AbsTelegramFragment extends Fragment {
    @App
    protected TelegramApplication application;

    protected void showError(TdApi.Error error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.error).setMessage(error.text);
        builder.show();
    }

    protected void onError(TdApi.Error error) {
        showError(error);
    }
}
