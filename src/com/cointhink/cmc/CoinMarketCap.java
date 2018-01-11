package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class CoinMarketCap {
    public static List<String> parse(String json) {
        try {
            JSONArray arr = new JSONArray(json);
            Log.d(Constants.APP_TAG, "coinmarketcap count" + arr.length());
        } catch (JSONException e) {
            String eStr = e.toString();
            Log.e(Constants.APP_TAG,
                    "Error parsing f/e " + eStr.substring(0, 200)
                            + eStr.substring(eStr.length() - 200));
        }
        return new ArrayList<>();
    }

}
