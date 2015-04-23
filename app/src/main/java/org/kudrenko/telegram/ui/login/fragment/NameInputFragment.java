package org.kudrenko.telegram.ui.login.fragment;

import android.widget.EditText;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;

@EFragment(R.layout.fragment_login_name_input)
public class NameInputFragment extends AbsLoginFragment {
    @ViewById(R.id.first_name)
    EditText firstName;

    @ViewById(R.id.last_name)
    EditText lastName;

    public void onConfirm() {
        String firstNameStr = firstName.getText().toString().trim();
        String lastNameStr = lastName.getText().toString().trim();
        send(new TdApi.AuthSetName(firstNameStr, lastNameStr), resultHandler());
    }
}
