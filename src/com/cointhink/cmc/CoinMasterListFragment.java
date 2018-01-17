package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CoinMasterListFragment extends Fragment implements CacheCallbacks {

    private Cache cache;
    private List<Coin> coinList = new ArrayList<>();
    private Gooey gooey;
    private IconMgr iconMgr;
    private Database db;

    // db = new Database(getApplicationContext());
    // iconMgr = new IconMgr(getApplicationContext());
    // gooey = new Gooey(this, coinList, iconMgr);
    // cache = new Cache(this);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist. The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed. Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        return inflater.inflate(R.layout.list_all_fragment,
                container, false);
    }



    @Override
    public void cacheUpdateDone(List<Coin> coins) {
        Log.d(Constants.APP_TAG, "cacheUpdateDone()");
        if (coins != null) {
            gooey.fetchErr("");
            gooey.add(coins);
            gooey.topTime(cache.last);
            gooey.countFreshen();
            gooey.refreshing(false);
        }
    }

    @Override
    public void cacheUpdateStarted() {
        Log.d(Constants.APP_TAG, "cacheUpdateStarted()");
        gooey.refreshing(true);
    }

    @Override
    public void cacheErr(String err) {
        gooey.fetchErr(err);
    }
}
