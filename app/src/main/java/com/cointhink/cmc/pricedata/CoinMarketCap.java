package com.cointhink.cmc.pricedata;

import android.util.Log;

import com.cointhink.cmc.Coin;
import com.cointhink.cmc.Constants;
import com.cointhink.cmc.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CoinMarketCap implements Provider {

    public static String NAME = "coinmarketcap.com";
    public static String COIN_URL = "https://pro-api.coinmarketcap.com/public-api/v3/cryptocurrency/listings/latest?limit=200";

    @Override
    public List<Coin> parse(String json, Database db) {
        ArrayList<Coin> coins = null; // null signals err
        try {
            coins = new ArrayList<>();
            JSONObject data = new JSONObject(json);
            JSONArray arr = data.getJSONArray("data");
            Log.d(Constants.APP_TAG,
                    "coinmarketcap parsing " + arr.length() + " records");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Coin coin = coinFormat(o);
                coin.favorited = db.isFavorited(coin);
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

    @Override
    public Coin coinFormat(JSONObject o) throws JSONException {
        Coin coin = new Coin();
        coin.name = o.getString("name");
        coin.symbol = o.getString("symbol");
        JSONArray quotes = o.getJSONArray("quote");
        JSONObject quote = quotes.getJSONObject(0);
        coin.price = quote.getString("price");
        coin.marketCap = quote.optString("market_cap", "0");
        coin.vol_24h = quote.optString("volume_24h", "0");
        coin.chg_1h = quote.optString("percent_change_1h", "0");
        coin.chg_24h = quote.optString("percent_change_24h", "0");
        coin.chg_7d = quote.optString("percent_change_7d", "0");

        coin.img_url = imgUrl(coin.name);
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

    static String imgUrl(String name) {
        //https://s2.coinmarketcap.com/static/img/coins/32x32/1027.png
        //https://s2.coinmarketcap.com/static/img/coins/64x64/1.png
        String url = "https://files.coinmarketcap.com/static/img/coins/64x64/";
        String urlName = name.toLowerCase().replaceAll(" ", "-");
        return url + urlName + ".png";
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
