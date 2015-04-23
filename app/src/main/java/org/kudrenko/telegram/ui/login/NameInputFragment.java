package org.kudrenko.telegram.ui.login;

import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.ui.AbsTelegramFragment;

@EFragment(R.layout.fragment_login_name_input)
public class NameInputFragment extends AbsTelegramFragment {
    @ViewById(R.id.first_name)
    EditText firstName;

    @ViewById(R.id.last_name)
    EditText lastName;

    @Click(R.id.menu_options_icon)
    void onConfirm() {
        String firstNameStr = firstName.getText().toString().trim();
        String lastNameStr = lastName.getText().toString().trim();
        application.send(new TdApi.AuthSetName(firstNameStr, lastNameStr), new Client.ResultHandler() {
            @Override
            public void onResult(TdApi.TLObject object) {

            }
        });
    }
}
