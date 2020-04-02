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
    public static String COIN_URL = "https://api.coincap.io/v2/assets";

    @Override
    public List<Coin> parse(String json, Database db) {
        ArrayList<Coin> coins = null;
        try {
            coins = new ArrayList<>();
            JSONObject data = new JSONObject(json);
            JSONArray arr = data.getJSONArray("data");
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
        coin.name = o.getString("name");
        coin.symbol = o.getString("symbol");
        coin.price = o.getString("priceUsd");
        coin.marketCap = ""+o.getLong("marketCapUsd");
        // coin.chg_1h = o.getString("percent_change_1h");
        coin.chg_24h = String.format("%.1f",+o.getDouble("changePercent24Hr"));
        // coin.chg_7d = o.getString("percent_change_7d");
        coin.vol_24h = String.format("%.0f",+o.getDouble("volumeUsd24Hr"));
        coin.img_url = imgUrl(coin.symbol);

        return coin;
        /*
2020-04-01
    {
      "id": "bitcoin",
      "rank": "1",
      "symbol": "BTC",
      "name": "Bitcoin",
      "supply": "18301187.0000000000000000",
      "maxSupply": "21000000.0000000000000000",
      "marketCapUsd": "126014353855.4189737546047116",
      "volumeUsd24Hr": "9431702703.6253487401390177",
      "priceUsd": "6885.5836430401467268",
      "changePercent24Hr": "11.3614224104286762",
      "vwap24Hr": "6584.9490635647270986"
    },

         * old
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

    static String imgUrl(String name) {
        String url = "https://static.coincap.io/assets/icons/";
        String urlName = name.toLowerCase().replaceAll(" ", "-");
        return url + urlName + "@2x.png";
    }

    @Override
    public String getDisplayName() {
        return NAME;
    }
}
