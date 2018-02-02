package com.cointhink.cmc.pricedata;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cointhink.cmc.Coin;
import com.cointhink.cmc.Constants;
import com.cointhink.cmc.Database;

import android.util.Log;

public class CoinCapIo implements Provider {
    public static String NAME = "coincap.io";
    public static String COIN_URL = "http://coincap.io/front";

    @Override
    public List<Coin> parse(String json, Database db) {
        ArrayList<Coin> coins = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            Log.d(Constants.APP_TAG,
                    "CoinCapIo parsing " + arr.length() + " records");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Coin coin = coinFormat(o);
                coin.favorited = db.isFavorited(coin);
                coins.add(coin);
            }
        } catch (JSONException e) {
            Log.e(Constants.APP_TAG, "Error parsing f/e " + e);
        }
        return coins;
    }

    @Override
    public Coin coinFormat(JSONObject o) throws JSONException {
        Log.d(Constants.APP_TAG, ""+o);
        Coin coin = new Coin();
        coin.name = o.getString("long");
        coin.symbol = o.getString("short");
        coin.price = o.getString("price");
        coin.marketCap = ""+o.getLong("mktcap");
        // coin.chg_1h = o.getString("percent_change_1h");
        coin.chg_24h = o.getString("perc");
        // coin.chg_7d = o.getString("percent_change_7d");
        coin.vol_24h = ""+o.getLong("usdVolume");
        coin.img_url = CoinMarketCap.imgUrl(coin.name);

        return coin;
        /*
         * { "cap24hrChange": -11.72, "long": "Bitcoin", "mktcap": 175041129270,
         * "perc": -11.72, "price": 10406.2, "shapeshift": true, "short": "BTC",
         * "supply": 16820850, "usdVolume": 10486200000, "volume": 10486200000,
         * "vwapData": 10737.268666201282, "vwapDataBTC": 10737.268666201282 },
         */
    }

    @Override
    public String getDataUrl() {
        return COIN_URL;
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}
