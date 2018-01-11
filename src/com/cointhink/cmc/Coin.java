package com.cointhink.cmc;

import org.json.JSONException;
import org.json.JSONObject;

public class Coin {
    public String name;
    public String symbol;
    public String price;

    public Coin(String name) {
        super();
        this.name = name;
    }

    public Coin(JSONObject o) throws JSONException {
        this.name = o.getString("name");
        this.symbol = o.getString("symbol");
        this.price = o.getString("price_usd");

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
