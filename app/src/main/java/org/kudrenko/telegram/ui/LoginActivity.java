package org.kudrenko.telegram.ui;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.otto.events.AuthStateUpdateEvent;
import org.kudrenko.telegram.ui.chat.ChatsActivity_;
import org.kudrenko.telegram.ui.common.AbsTelegramActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AbsTelegramActivity {

    @ViewById
    EditText phone;

    @ViewById
    EditText code;

    @ViewById
    Button loginBtn;

    @ViewById
    Button checkCode;

    @Click(R.id.loginBtn)
    void onLoginClick() {
        String inputPhone = phone.getText().toString();
        application.send(new TdApi.AuthSetPhoneNumber(inputPhone), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.i(TAG + " - AuthSetPhoneNumber", String.valueOf(object));
            }
        });
    }

    @Click(R.id.checkCode)
    void onCheckCode() {
        String inputCode = code.getText().toString();
        application.send(new TdApi.AuthSetCode(inputCode), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {
                Log.i(TAG + " - AuthSetCode", String.valueOf(object));
            }
        });
    }

    @Subscribe
    public void onAuthStateUpdate(AuthStateUpdateEvent event) {
        TdApi.AuthState state = event.state;
        if (state instanceof TdApi.AuthStateWaitSetPhoneNumber) {
            phone.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.VISIBLE);

            code.setVisibility(View.GONE);
            checkCode.setVisibility(View.GONE);
        } else if (state instanceof TdApi.AuthStateWaitSetCode) {
            phone.setVisibility(View.GONE);
            loginBtn.setVisibility(View.GONE);

            code.setVisibility(View.VISIBLE);
            checkCode.setVisibility(View.VISIBLE);
        } else if (state instanceof TdApi.AuthStateOk) {
            ChatsActivity_.intent(this).start();
            finish();
        }
    }
}
