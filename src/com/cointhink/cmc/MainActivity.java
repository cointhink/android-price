package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivity extends FragmentActivity
        implements CacheCallbacks, FavoriteHandler {

    private Cache cache;
    private Database db;
    private PagerAdapter pagerAdapter;
    private CoinMasterListFragment coinMasterList;
    private CoinFavoritesFragment coinFavorites;
    private List<Coin> coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        db = new Database(getApplicationContext()).open();
        Log.d(Constants.APP_TAG,
                "db open. count count " + db.rowCount(Database.TABLE_COINS));
        cache = new Cache(this, db);

        coinMasterList = new CoinMasterListFragment();
        coinFavorites = new CoinFavoritesFragment();

        if (findViewById(R.id.viewpager) != null) {
            if (savedInstanceState != null) {
                return;
            }
            setupFragments(coinMasterList, coinFavorites);
        }
        switchFragment(new PrefsFragment());
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

    protected void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.mainframe, fragment, "frags").commit();
    }

    @Override
    public void cacheUpdateDone(List<Coin> coins) {
        Log.d(Constants.APP_TAG, "cacheUpdateDone()");
        if (coins != null) {
            this.coins = coins;
            db.add(coins);
            fragCacheGoodFixup(coinMasterList, coins);
            ArrayList<Coin> favCoins = coinListFavFilter(coins);
            fragCacheGoodFixup(coinFavorites, favCoins);
        }
    }

    public ArrayList<Coin> coinListFavFilter(List<Coin> coins) {
        ArrayList<Coin> favCoins = new ArrayList<>();
        for (int i=0, s=coins.size(); i < s; i++) {
            Coin coin = coins.get(i);
            if(coin.favorited) {
                favCoins.add(coin);
            }
        }
        return favCoins;
    }

    public void fragCacheGoodFixup(CoinListFragment coinFrag,
            List<Coin> coins) {
        coinFrag.fetchErr("");
        coinFrag.add(coins);
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

    @Override
    public boolean favoriteToggle(Coin c) {
        if (c.favorited) {
            c.favorited = false;
        } else {
            c.favorited = true;
        }
        db.update(c);
        ArrayList<Coin> favCoins = coinListFavFilter(coins);
        fragCacheGoodFixup(coinFavorites, favCoins);
        return c.favorited;
    }
}
