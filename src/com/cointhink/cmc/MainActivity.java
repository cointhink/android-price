package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivity extends FragmentActivity implements CacheCallbacks {

    private Cache cache;
    private List<Coin> coinList = new ArrayList<>();
    private Gooey gooey;
    private IconMgr iconMgr;
    private Database db;
    private PagerAdapter pagerAdapter;
    private CoinMasterListFragment coinMasterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        db = new Database(getApplicationContext());
        iconMgr = new IconMgr(getApplicationContext());
        cache = new Cache(this);

        coinMasterList = new CoinMasterListFragment();
        coinMasterList.coinList = new ArrayList<>();
        coinMasterList.iconMgr = iconMgr;

        if (findViewById(R.id.viewpager) != null) {
            if (savedInstanceState != null) {
                return;
            }
            setupFragments(coinMasterList);
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
        Log.d(Constants.APP_TAG, "onResume");
    }

    @Override
    public void cacheUpdateDone(List<Coin> coins) {
        Log.d(Constants.APP_TAG, "cacheUpdateDone()");
        if (coins != null) {
            coinMasterList.fetchErr("");
            coinMasterList.add(coins);
            coinMasterList.topTime(cache.last);
            coinMasterList.countFreshen();
            coinMasterList.refreshing(false);
        }
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
