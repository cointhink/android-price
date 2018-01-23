package com.cointhink.cmc;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
    public static final String COINS_SUBREDDIT = "subreddit";
    public static final String COINS_FAVORITED = "favorited";

    public Database(Context context) {
        dbHelper = new OpenHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public Database open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory,
                int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(Constants.APP_TAG, "sql table " + TABLE_COINS + " created.");
            db.execSQL("CREATE TABLE " + TABLE_COINS + " (" + ROW_ID
                    + " integer primary key, " + COINS_SYMBOL
                    + " text," + COINS_ICON_URL + " text,"
                    + COINS_SUBREDDIT + " text," + COINS_FAVORITED + " boolean,"
                    + ROW_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                int newVersion) {
        }
    }

    int rowCount(String tableName) {
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void add(List<Coin> coins) {
        for (int i = 0; i < coins.size(); i++) {
            update(coins.get(i));
        }
    }

    void update(Coin coin) {
        Log.d(Constants.APP_TAG, "db update symbol "+coin.symbol);
        ContentValues attributes = coin.getAttributes();
        int id = findId(coin.symbol);
        if (id >= 0) {
            attributes.put(ROW_ID, id);
        }
        db.insertWithOnConflict(Database.TABLE_COINS, null, attributes,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    private int findId(String symbol) {
        int id = -1;
        Cursor cursor = db.query(Database.TABLE_COINS, null,
                COINS_SYMBOL + " = ?", new String[] { symbol }, null, null,
                null, null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(ROW_ID));
            Log.d(Constants.APP_TAG, "db find symbol "+symbol+": "+id);
        } else {
            Log.d(Constants.APP_TAG, "db not find symbol "+symbol);
        }
        cursor.close();
        return id;
    }

    public boolean isFavorited(Coin coin) {
        return false;
    }
}
