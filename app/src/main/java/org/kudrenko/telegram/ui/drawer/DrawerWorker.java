package org.kudrenko.telegram.ui.drawer;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.TelegramApplication;
import org.kudrenko.telegram.ui.LoginActivity_;

@EBean
public class DrawerWorker implements Drawer.OnDrawerListener, Drawer.OnDrawerItemClickListener {
    public static final int LOG_OUT_ITEM = 0;

    @RootContext
    protected Activity activity;

    @SystemService
    InputMethodManager inputMethodManager;

    public Drawer.Result initDrawer() {
        return new Drawer()
                .withActivity(activity)
                .withActionBarDrawerToggle(false)
                .withOnDrawerListener(this)
                .addDrawerItems(new PrimaryDrawerItem().withName(R.string.log_out))
                .withOnDrawerItemClickListener(this)
                .build();
    }

    @Override
    public void onDrawerOpened(View view) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null)
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    @Override
    public void onDrawerClosed(View view) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        switch (i) {
            case LOG_OUT_ITEM:
                ((TelegramApplication) activity.getApplication()).send(new TdApi.AuthReset(), new Client.ResultHandler() {
                    @Override
                    public void onResult(TdApi.TLObject object) {
                        LoginActivity_.intent(activity).start();
                        activity.finish();
                    }
                });
                break;
        }
    }
}
