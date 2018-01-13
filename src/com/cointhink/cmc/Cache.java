package com.cointhink.cmc;

import java.util.Date;
import java.util.List;

public class Cache implements FetchCallbacks {

    Date last;
    CacheCallbacks mainActivity;

    public Cache(CacheCallbacks mainActivity) {
        this.mainActivity = mainActivity;
    }

    public boolean refreshNeeded() {
        return true;
    }

    public void launchRefresh() {
        mainActivity.cacheUpdateStarted();
        Net.cmcGet("https://api.coinmarketcap.com/v1/ticker/?limit=50", this);
    }

    @Override
    public void stringFetched(String json) {
        if (json != null) {
            last = new Date();
            List<Coin> coins = CoinMarketCap.parse(json);
            mainActivity.cacheUpdateDone(coins);
        }
    }

}
