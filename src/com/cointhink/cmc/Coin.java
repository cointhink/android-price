package com.cointhink.cmc;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.util.Log;

public class Coin implements Sqlable {
    public String name;
    public String symbol;
    public String price;
    public String marketCap;
    public String vol_24h;
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
        cv.put(Database.COINS_FAVORITED, favorited ? "1" : "0");
        return cv;
    }

    public static Coin fromJson(String json) {
        Coin coin = new Coin();
        try {
            JSONObject obj = new JSONObject(json);
            coin.name = obj.getString("name");
            coin.symbol = obj.getString("symbol");
            coin.price = obj.getString("price");
            coin.chg_24h= obj.getString("chg_24h");
        } catch (JSONException e) {
            Log.e(Constants.APP_TAG, "Error parsing coin " + e);
        }
        return coin;
    }

    public String toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("symbol", symbol);
            obj.put("price", price);
            obj.put("chg_24h", chg_24h);
        } catch (JSONException e) {
            Log.e(Constants.APP_TAG, "Error encoding coin " + e);
        }
        return obj.toString();
    }
}
