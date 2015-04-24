package org.kudrenko.telegram.ui.login;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.kudrenko.telegram.R;
import org.kudrenko.telegram.model.Country;
import org.kudrenko.telegram.storage.CountriesDatabaseHelper;
import org.kudrenko.telegram.ui.common.AbsTelegramActivity;

@EActivity(R.layout.fragment_countries)
public class CountriesChooserActivity extends AbsTelegramActivity {
    @ViewById(android.R.id.list)
    ListView listView;

    protected SimpleCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SimpleCursorAdapter(this, R.layout.item_country, null,
                new String[]{CountriesDatabaseHelper.NAME, CountriesDatabaseHelper.CODE},
                new int[]{android.R.id.text1, android.R.id.text2}, 0) {
            @Override
            public void setViewText(TextView v, String text) {
                if (v.getId() == android.R.id.text2) {
                    super.setViewText(v, "+" + text);
                } else super.setViewText(v, text);
            }
        };
    }

    @AfterViews
    void afterViews() {
        listView.setAdapter(adapter);
        adapter.swapCursor(countries());
    }

    @Click(R.id.menu_icon)
    void onBackClick() {
        onBackPressed();
    }

    @ItemClick(android.R.id.list)
    void onCountrySelect(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(CountriesDatabaseHelper.NAME));
        int code = cursor.getInt(cursor.getColumnIndex(CountriesDatabaseHelper.CODE));
        Country country = new Country(name, code);

        setResult(RESULT_OK, new Intent().putExtra("country", country));
        finish();
    }
}
