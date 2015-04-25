package org.kudrenko.telegram.ui.drawer;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.TelegramApplication;
import org.kudrenko.telegram.model.Profile;
import org.kudrenko.telegram.otto.OttoBus;
import org.kudrenko.telegram.otto.events.UpdateFileEvent;
import org.kudrenko.telegram.ui.login.LoginActivity_;

@EBean
public class DrawerWorker implements Drawer.OnDrawerListener, Drawer.OnDrawerItemClickListener {
    public static final int LOG_OUT_ITEM = 0;

    @RootContext
    protected Activity activity;

    @App
    TelegramApplication application;

    @SystemService
    InputMethodManager inputMethodManager;

    @Bean
    OttoBus ottoBus;

    @AfterInject
    void afterInject() {
        ottoBus.register(this);
    }

    protected int fileId;

    @Subscribe
    public void onFileDownload(UpdateFileEvent event) {
        TdApi.FileLocal fileLocal = event.file;
        if (fileLocal.id == fileId) {}
        //todo something with new avatar
    }

    public Drawer.Result initDrawer(Profile profile) {
        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem().withName(profile.name).withEmail(profile.phone);
        if (profile.file.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR) {
            fileId = ((TdApi.FileEmpty) profile.file).id;
            application.send(new TdApi.DownloadFile(fileId));
        } else if (profile.file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
            profileDrawerItem.withIcon(((TdApi.FileLocal) profile.file).path);
        }

        AccountHeader.Result header = new AccountHeader()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(profileDrawerItem)
                .build();

        return new Drawer()
                .withActivity(activity)
                .withActionBarDrawerToggle(false)
                .withOnDrawerListener(this)
                .addDrawerItems(new PrimaryDrawerItem().withName(R.string.log_out).withIcon(R.drawable.ic_logout))
                .withOnDrawerItemClickListener(this)
                .withAccountHeader(header)
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
                application.send(new TdApi.AuthReset(), new Client.ResultHandler() {
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
