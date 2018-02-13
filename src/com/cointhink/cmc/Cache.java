package com.cointhink.cmc;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import com.cointhink.cmc.http.CoinResponse;
import com.cointhink.cmc.http.FetchCallbacks;
import com.cointhink.cmc.pricedata.Provider;

import android.util.Log;

public class Cache {

    Date last = new Date(0);
    CacheCallbacks activity;
    private Database db;

    public Cache(CacheCallbacks mainActivity, Database db) {
        this.activity = mainActivity;
        this.db = db;
    }

    public boolean refreshNeeded() {
        long diffSec = ((new Date()).getTime() - last.getTime()) / 1000;
        Log.d(Constants.APP_TAG, "refreshNeeded: " + diffSec + " sec. old");
        return diffSec > 30;
    }

    public void launchRefresh(Provider provider) {
        activity.cacheUpdateStarted();
        Log.d(Constants.APP_TAG, "cachelaunchRefresh using " + provider);
        Net.cmcGet(null, provider.getDataUrl(), new OnFetched(provider));
    }

    class OnFetched implements FetchCallbacks {
        Provider provider;

        public OnFetched(Provider provider) {
            this.provider = provider;
        }

        @Override
        public void bytesFetched(CoinResponse request) {
            String json;
            try {
                if (request.data != null) {
                    json = new String(request.data, "UTF-8");
                    last = new Date();
                    List<Coin> coins = provider.parse(json, db);
                    activity.cacheUpdateDone(coins, provider);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void progressUpdate(Integer i) {
            if (i == -1) {
                activity.cacheErr("dns error");
            }
            if (i == -2) {
                activity.cacheErr("parsing");
            }
        }
    }
}
