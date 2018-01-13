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
            Log.d(Constants.APP_TAG,
                    "coinmarketcap parsing " + arr.length() + " records");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Coin coin = coinFormat(o);
                coins.add(coin);
            }
        } catch (JSONException e) {
            String eStr = e.toString();
            String eMsg = eStr.length() > 199 ? eStr.substring(0, 200)
                    + eStr.substring(eStr.length() - 200) : eStr;
            Log.e(Constants.APP_TAG, "Error parsing f/e " + eMsg);
        }
        return coins;
    }

    public static Coin coinFormat(JSONObject o) throws JSONException {
        Coin coin = new Coin();
        coin.name = o.getString("name");
        coin.symbol = o.getString("symbol");
        coin.price = o.getString("price_usd");
        coin.marketCap = o.getString("market_cap_usd");
        coin.chg_1h = o.getString("percent_change_1h");
        coin.chg_24h = o.getString("percent_change_24h");
        coin.chg_7d = o.getString("percent_change_7d");
        return coin;
        /*
         * { "id": "bitcoin", "name": "Bitcoin", "symbol": "BTC", "rank": "1",
         * "price_usd": "13105.9", "price_btc": "1.0", "24h_volume_usd":
         * "17107800000.0", "market_cap_usd": "220115228738",
         * "available_supply": "16795125.0", "total_supply": "16795125.0",
         * "max_supply": "21000000.0", "percent_change_1h": "-5.05",
         * "percent_change_24h": "-8.94", "percent_change_7d": "-13.43",
         * "last_updated": "1515646461" }
         *
         */
    }



}
