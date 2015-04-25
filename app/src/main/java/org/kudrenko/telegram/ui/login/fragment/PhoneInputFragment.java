package org.kudrenko.telegram.ui.login.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.drinkless.td.libcore.telegram.TdApi;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.model.Country;
import org.kudrenko.telegram.ui.login.CountriesChooserActivity_;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@EFragment(R.layout.fragment_login_phone_input)
public class PhoneInputFragment extends AbsLoginFragment {
    public static final int REQUEST_CODE_COUNTRY = 200;

    @ViewById(R.id.country)
    EditText countryEtx;

    @ViewById(R.id.country_code)
    EditText countryCode;

    @ViewById(R.id.number)
    EditText phoneEtx;

    @SystemService
    LocationManager locationManager;

    protected Country country;

    @Click(R.id.country)
    void onCountryNameClick() {
        CountriesChooserActivity_.intent(this).startForResult(REQUEST_CODE_COUNTRY);
    }

    @Override
    public void onResume() {
        super.onResume();
        phoneEtx.requestFocus();
        if (country == null) {
            try {
                String lastKnownCountry = getLastKnownCountry();
                Country country = find(lastKnownCountry);
                setCountry(country);
            } catch (IOException e) {
                //nothing
            }
        }
    }

    private Country find(String country) {
        return application.helper.findByName(country);
    }

    private String getLastKnownCountry() throws IOException {
        Location location = getLastKnownLocation();
        if (location != null) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
        }
        return null;
    }

    private Location getLastKnownLocation() {
        Location location = null;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location;
    }

    @Override
    public void onConfirm() {
        String phoneStr = phoneEtx.getText().toString().trim();
        String countryCodeStr = countryCode.getText().toString().trim();
        send(new TdApi.AuthSetPhoneNumber(countryCodeStr + phoneStr), resultHandler());
    }

    @OnActivityResult(REQUEST_CODE_COUNTRY)
    void onCountrySelect(@OnActivityResult.Extra(value = "country") Country selected) {
        setCountry(selected);
    }

    private void setCountry(Country selected) {
        if (selected != null) {
            country = selected;
            countryEtx.setText(selected.name);
            countryCode.setText(String.valueOf(selected.code));
        }
    }

    @AfterTextChange(R.id.country_code)
    void onCountryCodeChange(Editable s, TextView view) {
        if (s.length() == 0 || s.charAt(0) != '+') {
            view.setText("+" + s);
        }
    }
}
