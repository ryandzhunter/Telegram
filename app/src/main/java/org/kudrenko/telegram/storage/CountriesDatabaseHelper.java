package org.kudrenko.telegram.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.io.IOException;
import java.io.InputStream;

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

    public Cursor countries(SQLiteDatabase db) {
        return db.query(TABLE_NAME, new String[]{BaseColumns._ID, NAME, CODE}, null, null, null, null, NAME + " ASC");
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public Cursor findByName(String country) {
        return getReadableDatabase().query(TABLE_NAME, new String[] {NAME, CODE}, NAME + " like ?", new String[]{country}, null, null, null);
    }
}
