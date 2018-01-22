package com.cointhink.cmc;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivity extends FragmentActivity implements CacheCallbacks {

    private Cache cache;
    private IconMgr iconMgr;
    private Database db;
    private PagerAdapter pagerAdapter;
    private CoinMasterListFragment coinMasterList;
    private CoinFavoritesFragment coinFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        db = new Database(getApplicationContext());
        cache = new Cache(this);

        coinMasterList = new CoinMasterListFragment();
        coinFavorites = new CoinFavoritesFragment();

        if (findViewById(R.id.viewpager) != null) {
            if (savedInstanceState != null) {
                return;
            }
            setupFragments(coinMasterList, coinFavorites);
        }
    }

    private void setupFragments(Fragment... fments) {
        List<Fragment> fragments = new Vector<>();
        for (int i = 0; i < fments.length; i++) { // such Java
            fragments.add(fments[i]);
        }
        this.pagerAdapter = new PagerAdapter(super.getSupportFragmentManager(),
                fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
        pager.setAdapter(this.pagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.APP_TAG, "MainActivity onResume");
        if (cache.refreshNeeded()) {
            Log.d(Constants.APP_TAG, "refreshNeeded. launchRefresh.");
            cache.launchRefresh();
        }
    }

    @Override
    public void cacheUpdateDone(List<Coin> coins) {
        Log.d(Constants.APP_TAG, "cacheUpdateDone()");
        if (coins != null) {
            fragCacheGoodFixup(coinMasterList, coins);
            fragCacheGoodFixup(coinFavorites, coins);
        }
    }

    public void fragCacheGoodFixup(CoinListFragment coinFrag, List<Coin> coins) {
        coinFrag.fetchErr("");
        coinFrag.add(coins);
        db.add(coins);
        coinFrag.topTime(cache.last);
        coinFrag.countFreshen();
        coinFrag.refreshing(false);
    }

    @Override
    public void cacheUpdateStarted() {
        Log.d(Constants.APP_TAG, "cacheUpdateStarted()");
        coinMasterList.refreshing(true);
    }

    @Override
    public void cacheErr(String err) {
        coinMasterList.fetchErr(err);
    }
}
