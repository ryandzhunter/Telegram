package org.kudrenko.telegram.ui.login;

import android.widget.EditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.ui.AbsTelegramFragment;

@EFragment(R.layout.fragment_login_phone_input)
public class PhoneInputFragment extends AbsTelegramFragment {

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
        application.send(new TdApi.AuthSetPhoneNumber(phoneStr), resultHandler());
    }
}
