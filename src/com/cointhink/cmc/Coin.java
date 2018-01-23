package com.cointhink.cmc;

import android.content.ContentValues;


public class Coin implements Sqlable {
    public String name;
    public String symbol;
    public String price;
    public String marketCap;
    public String chg_1h;
    public String chg_24h;
    public String chg_7d;
    public String img_url;
    public boolean favorited;

    @Override
    public String getTableName() {
        return Database.TABLE_COINS;
    }

    @Override
    public ContentValues getAttributes() {
        ContentValues cv = new ContentValues();
        cv.put(Database.COINS_SYMBOL, symbol);
        cv.put(Database.COINS_FAVORITED, ""+favorited);
        return cv;
    }
}
