package com.cointhink.price;

import android.util.Log;

import com.cointhink.price.http.FetchCallbacks;
import com.cointhink.price.http.Response;
import com.cointhink.price.pricedata.Provider;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

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
        Log.d(Constants.APP_TAG, "refreshcheck: cache " + diffSec + " sec. old");
        return diffSec > (60 * 3);
    }

    public void launchRefresh(Provider provider, String api_key) {
        activity.cacheUpdateStarted();
        Log.d(Constants.APP_TAG, "cachelaunchRefresh using " + provider);
        Net.cmcGet(null, provider.getDataUrl(), api_key, new OnFetched(provider));
    }

    class OnFetched implements FetchCallbacks {
        Provider provider;

        public OnFetched(Provider provider) {
            this.provider = provider;
        }

        @Override
        public void bytesFetched(Response request) {
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
        public void progressUpdate(String s) {
            activity.cacheErr(s);
        }
    }
}
