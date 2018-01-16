package com.cointhink.cmc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database {
    private SQLiteDatabase db;
    private final OpenHelper dbHelper;

    private final String DATABASE_NAME = "prices";
    public static final int DATABASE_VERSION = 1;
    public static final String ROW_ID = "_id";
    public static final String ROW_CREATED_AT = "created_at";

    /* Users table */
    public static final String TABLE_COINS = "coins";
    public static final String COINS_SYMBOL = "symbol";
    public static final String COINS_ICON_URL = "icon_url";
    public static final String COINS_SUBREDDIT= "subreddit";
    public static final boolean COINS_FAVORITE = false;

    public Database(Context context) {
        dbHelper = new OpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory,
                int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(Constants.APP_TAG, "sql table "+TABLE_COINS+" created.");
            db.execSQL("CREATE TABLE "+TABLE_COINS+" ("+
                    ROW_ID+" integer primary key, "+
                    COINS_SYMBOL + " text," +
                    COINS_ICON_URL + " text," +
                    COINS_SUBREDDIT + " text," +
                    COINS_FAVORITE + " boolean," +
                    ROW_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
