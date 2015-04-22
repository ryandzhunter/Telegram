package org.kudrenko.telegram.ui.login;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.TelegramApplication;

@EFragment(R.layout.fragment_login_code_input)
public class CodeInputFragment extends Fragment {

    @App
    TelegramApplication application;

    @ViewById(R.id.code)
    EditText codeEtx;

    @Click(R.id.menu_options_icon)
    void onConfirm() {
        String codeStr = codeEtx.getText().toString().trim();
        if (!TextUtils.isEmpty(codeStr)) {
            application.send(new TdApi.AuthSetCode(codeStr), new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.TLObject object) {
                    if (object instanceof TdApi.Error) {
                        showWrongCodeError();
                    }
                }
            });
        } else showEmptyCodeError();
    }

    @Click(R.id.menu_icon)
    void onBackClick() {
        getActivity().onBackPressed();
    }

    private void showWrongCodeError() {
        //todo
    }

    private void showEmptyCodeError() {
        String error = getString(R.string.input_code);
        codeEtx.setError(error);
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }
}
