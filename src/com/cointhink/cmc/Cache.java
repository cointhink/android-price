package com.cointhink.cmc;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

public class Cache implements FetchCallbacks {

    Date last;
    CacheCallbacks mainActivity;
    private Database db;

    public Cache(CacheCallbacks mainActivity, Database db) {
        this.mainActivity = mainActivity;
        this.db = db;
    }

    public boolean refreshNeeded() {
        return true;
    }

    public void launchRefresh() {
        mainActivity.cacheUpdateStarted();
        Net.cmcGet(null, CoinMarketCap.COIN_URL, this);
    }

    @Override
    public void bytesFetched(HttpResponse request) {
        String json;
        try {
            if (request.data != null) {
                json = new String(request.data,"UTF-8");
                last = new Date();
                List<Coin> coins = CoinMarketCap.parse(json, db);
                mainActivity.cacheUpdateDone(coins);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void progressUpdate(Integer i) {
        if (i == -1) {
            mainActivity.cacheErr("dns error");
        }
    }
}
