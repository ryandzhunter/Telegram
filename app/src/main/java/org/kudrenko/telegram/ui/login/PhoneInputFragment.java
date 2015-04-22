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

@EFragment(R.layout.fragment_login_phone_input)
public class PhoneInputFragment extends Fragment {

    @App
    TelegramApplication application;

    @ViewById(R.id.number)
    EditText phoneEtx;

    @Click(R.id.country_code)
    void onCountryCodeClick() {
        countrySelect();
    }

    @Click(R.id.country)
    void onCountryNameClick() {
        countrySelect();
    }

    private void countrySelect() {
        ((LoginActivity) getActivity()).showCountriesDialog();
    }

    @Click(R.id.menu_options_icon)
    void onConfirm() {
        String phoneStr = phoneEtx.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneStr)) {
            application.send(new TdApi.AuthSetPhoneNumber(phoneStr), new Client.ResultHandler() {
                @Override
                public void onResult(TdApi.TLObject object) {
                    if (object instanceof TdApi.Error) {
                        showWrongPhoneError();
                    }
                }
            });
        } else showEmptyPhoneError();
    }

    private void showWrongPhoneError() {
        //todo
    }

    private void showEmptyPhoneError() {
        String error = getString(R.string.input_phone);
        phoneEtx.setError(error);
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }
}
