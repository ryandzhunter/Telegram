package org.kudrenko.telegram.ui.common;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.TelegramApplication;
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
