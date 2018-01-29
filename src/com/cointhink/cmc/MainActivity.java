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
        implements CacheCallbacks, FavoriteHandler, FragmentReadyListener {

    private Cache cache;
    private Database db;
    private PagerAdapter pagerAdapter;
    private List<Coin> coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        db = new Database(getApplicationContext()).open();
        Log.d(Constants.APP_TAG, "MainActivity onCreate db open. count count "
                + db.rowCount(Database.TABLE_COINS));
        cache = new Cache(this, db);

        if (findViewById(R.id.viewpager) != null) {
            CoinMasterListFragment masterFrag = new CoinMasterListFragment();
            Log.d(Constants.APP_TAG,
                    "MainActivity onCreate made masterFrag " + masterFrag);
            setupFragments(masterFrag, new CoinFavoritesFragment(),
                    new PrefsFragment());
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
        Log.d(Constants.APP_TAG, "MainActivity onResume.");
    }

    @Override
    public void onFragementReady() {
        Log.d(Constants.APP_TAG, "MainActivity onFragmentReady.");
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
        if (coins == null) {
        } else {
            this.coins = coins;
            db.add(coins);
            CoinListFragment masterFrag =
                    //(CoinListFragment) getSupportFragmentManager()
                    //.findFragmentById(R.id.masterListFragment);
             (CoinListFragment) pagerAdapter.getItem(0);
            Log.d(Constants.APP_TAG,
                    "cacheUpdateDone found masterFrag " + masterFrag);
            fragCacheGoodFixup(masterFrag, coins);
            ArrayList<Coin> favCoins = coinListFavFilter(coins);
            CoinListFragment favoritesFrag = (CoinListFragment) pagerAdapter.getItem(1);
            Log.d(Constants.APP_TAG, "cacheUpdateDone found favoritesFrag "+favoritesFrag);
            fragCacheGoodFixup(favoritesFrag, favCoins);
        }
    }

    public ArrayList<Coin> coinListFavFilter(List<Coin> coins) {
        ArrayList<Coin> favCoins = new ArrayList<>();
        for (int i = 0, s = coins.size(); i < s; i++) {
            Coin coin = coins.get(i);
            if (coin.favorited) {
                favCoins.add(coin);
            }
        }
        return favCoins;
    }

    public void fragCacheGoodFixup(CoinListFragment fragment,
            List<Coin> coins) {
        fragment.fetchErr("");
        fragment.add(coins);
        fragment.topTime(cache.last);
        fragment.countFreshen();
        fragment.refreshing(false);
    }

    @Override
    public void cacheUpdateStarted() {
        ((CoinListFragment) pagerAdapter.getItem(0)).refreshing(true);
    }

    @Override
    public void cacheErr(String err) {
        ((CoinListFragment) pagerAdapter.getItem(0)).fetchErr(err);
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
        fragCacheGoodFixup((CoinListFragment) pagerAdapter.getItem(1),
                favCoins);
        return c.favorited;
    }

}
