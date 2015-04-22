package org.kudrenko.telegram.ui.login;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.EActivity;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.otto.events.AuthStateUpdateEvent;
import org.kudrenko.telegram.ui.chat.ChatsActivity_;
import org.kudrenko.telegram.ui.common.AbsTelegramActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AbsTelegramActivity {

    @Subscribe
    public void onAuthStateUpdate(AuthStateUpdateEvent event) {
        TdApi.AuthState state = event.state;
        if (state instanceof TdApi.AuthStateWaitSetPhoneNumber) {
            showPhoneDialog();
        } else if (state instanceof TdApi.AuthStateWaitSetCode) {
            showCodeDialog();
        } else if (state instanceof TdApi.AuthStateWaitSetName) {
            showNameDialog();
        } else if (state instanceof TdApi.AuthStateOk) {
            ChatsActivity_.intent(this).start();
            finish();
        }
    }

    public void showPhoneDialog() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, PhoneInputFragment_.builder().build())
                .addToBackStack(null)
                .commit();
    }

    public void showCountriesDialog() {

    }

    public void showCodeDialog() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, CodeInputFragment_.builder().build())
                .addToBackStack(null)
                .commit();
    }

    public void showNameDialog() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStackImmediate();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }
}
