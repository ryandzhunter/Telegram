package org.kudrenko.telegram.ui.login;

import android.support.v4.app.FragmentTransaction;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.EActivity;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.otto.events.AuthStateUpdateEvent;
import org.kudrenko.telegram.ui.AbsTelegramFragment;
import org.kudrenko.telegram.ui.chat.ChatsActivity_;
import org.kudrenko.telegram.ui.common.AbsTelegramActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AbsTelegramActivity {

    @Subscribe
    public void onAuthStateUpdate(AuthStateUpdateEvent event) {
        TdApi.AuthState state = event.state;
        if (state.getConstructor() == TdApi.AuthStateWaitSetPhoneNumber.CONSTRUCTOR) {
            showPhoneDialog();
        } else if (state.getConstructor() == TdApi.AuthStateWaitSetCode.CONSTRUCTOR) {
            showCodeDialog();
        } else if (state.getConstructor() == TdApi.AuthStateWaitSetName.CONSTRUCTOR) {
            showNameDialog();
        } else if (state.getConstructor() == TdApi.AuthStateOk.CONSTRUCTOR) {
            ChatsActivity_.intent(this).start();
            finish();
        }
    }

    public void showPhoneDialog() {
        openFragment(PhoneInputFragment_.builder().build(), false);
    }

    public void showCountriesDialog() {
        openFragment(CountriesChooserFragment_.builder().build(), true);
    }

    public void showCodeDialog() {
        openFragment(CodeInputFragment_.builder().build(), true);
    }

    public void showNameDialog() {
        openFragment(NameInputFragment_.builder().build(), true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStackImmediate();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }

    protected void openFragment(AbsTelegramFragment fragment, boolean withSlidingAnimation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (withSlidingAnimation) {
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        transaction.replace(R.id.content, fragment).addToBackStack(null).commit();
    }
}
