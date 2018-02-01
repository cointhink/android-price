package com.cointhink.cmc;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import com.cointhink.cmc.pricedata.Provider;

import android.util.Log;

public class Cache {

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

    public void launchRefresh(Provider provider) {
        mainActivity.cacheUpdateStarted();
        Log.d(Constants.APP_TAG, "cachelaunchRefresh using "+provider);
        Net.cmcGet(null, provider.getDataUrl(), new OnFetched(provider));
    }

    class OnFetched implements FetchCallbacks {
        Provider provider;

        public OnFetched(Provider provider) {
            this.provider = provider;
        }

        @Override
        public void bytesFetched(HttpResponse request) {
            String json;
            try {
                if (request.data != null) {
                    json = new String(request.data, "UTF-8");
                    last = new Date();
                    List<Coin> coins = provider.parse(json, db);
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
}
