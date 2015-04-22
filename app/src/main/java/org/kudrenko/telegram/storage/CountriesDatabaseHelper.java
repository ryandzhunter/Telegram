package org.kudrenko.telegram.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by delphin on 22.04.2015.
 */
public class CountriesDatabaseHelper extends SQLiteOpenHelper {
    protected Context context;

    public CountriesDatabaseHelper(Context context) {
        super(context, "countries", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getSql());
    }

    private String getSql() {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("countries.sql");
            return convertStreamToString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Countries.sql is invalid or doesn't exist");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
