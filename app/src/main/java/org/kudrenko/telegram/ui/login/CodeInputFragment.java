package org.kudrenko.telegram.ui.login;

import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.ui.AbsTelegramFragment;

@EFragment(R.layout.fragment_login_code_input)
public class CodeInputFragment extends AbsTelegramFragment {

    @ViewById(R.id.code)
    EditText codeEtx;

    @Click(R.id.menu_options_icon)
    void onConfirm() {
        String codeStr = codeEtx.getText().toString().trim();
        application.send(new TdApi.AuthSetCode(codeStr), resultHandler());
    }

    @Click(R.id.menu_icon)
    void onBackClick() {
        ((LoginActivity) getActivity()).close(this);
    }
}
