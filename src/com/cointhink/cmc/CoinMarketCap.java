package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class CoinMarketCap {
    public static List<Coin> parse(String json) {
        ArrayList<Coin> coins = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            Log.d(Constants.APP_TAG, "coinmarketcap count" + arr.length());
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Coin coin = new Coin(o);
                coins.add(coin);
            }
        } catch (JSONException e) {
            String eStr = e.toString();
            String eMsg = eStr.length() > 199 ? eStr.substring(0, 200)
                    + eStr.substring(eStr.length() - 200) : eStr;
            Log.e(Constants.APP_TAG,
                    "Error parsing f/e " + eMsg);
        }
        return coins;
    }

}
