package org.kudrenko.telegram.ui.login.fragment;

import android.widget.EditText;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;

@EFragment(R.layout.fragment_login_code_input)
public class CodeInputFragment extends AbsLoginFragment {

    @ViewById(R.id.code)
    EditText codeEtx;

    public void onConfirm() {
        String codeStr = codeEtx.getText().toString().trim();
        send(new TdApi.AuthSetCode(codeStr), resultHandler());
    }
}
