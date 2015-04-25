package org.kudrenko.telegram.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.kudrenko.telegram.model.Country;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CountriesDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "countries";

    public static final String TABLE_NAME = "country";

    public static final String NAME = "nicename";
    public static final String CODE = "phonecode";

    protected Context context;

    public CountriesDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateSql());
        db.execSQL(getInsertSql());
    }

    private String getCreateSql() {
        return getAssetSql("countries_create.sql");
    }

    private String getInsertSql() {
        return getAssetSql("countries_insert.sql");
    }

    private String getAssetSql(String assetName) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(assetName);
            return convertStreamToString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Countries.sql is invalid or doesn't exist");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Country> countries() {
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, new String[]{BaseColumns._ID, NAME, CODE}, null, null, null, null, NAME + " ASC");
        List<Country> countries = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            countries.add(fromCursor(cursor));
        }
        return countries;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public Country findByName(String country) {
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, new String[]{NAME, CODE}, NAME + " like ?", new String[]{country}, null, null, null);
        if (cursor.moveToFirst()) {
            return fromCursor(cursor);
        }
        return null;
    }

    private Country fromCursor(Cursor cursor) {
        return new Country(cursor.getString(cursor.getColumnIndex(NAME)), cursor.getInt(cursor.getColumnIndex(CODE)));
    }
}
