package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cointhink.cmc.pricedata.CoinCapIo;
import com.cointhink.cmc.pricedata.CoinMarketCap;
import com.cointhink.cmc.pricedata.Provider;
import com.cointhink.cmc.ui.CoinDetail;
import com.cointhink.cmc.ui.CoinFavoritesFragment;
import com.cointhink.cmc.ui.CoinListFragment;
import com.cointhink.cmc.ui.CoinMasterListFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class MainActivity extends FragmentActivity implements CacheCallbacks,
        FavoriteHandler, FragmentReadyListener, OnPageChangeListener {

    private Cache cache;
    private Database db;
    private PagerAdapter pagerAdapter;
    private List<Coin> coins;
    private Prefs prefs;
    private List<Provider> providers;
    private CoinDetail detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.APP_TAG,
                "MainActivity onCreate bundle: "
                        + ((savedInstanceState == null) ? "" : "true")
                        + " pagerAdapter: " + pagerAdapter);
        setContentView(R.layout.mainframe);

        prefs = new Prefs(this);

        db = new Database(getApplicationContext()).open();

        Log.d(Constants.APP_TAG, "MainActivity onCreate db open. coin count "
                + db.rowCount(Database.TABLE_COINS));
        cache = new Cache(this, db);
        providers = new ArrayList<>();
        providers.add(new CoinMarketCap());
        providers.add(new CoinCapIo());

        if (findViewById(R.id.viewpager) != null) {
            CoinMasterListFragment masterFrag = new CoinMasterListFragment();
            CoinFavoritesFragment favoritesFrag = new CoinFavoritesFragment();
            detailFragment = new CoinDetail();
            setupFragments(masterFrag, favoritesFrag, new PrefsFragment());
        }
    }

    private void setupFragments(Fragment... fments) {
        List<Fragment> fragments = new Vector<>();
        for (int i = 0; i < fments.length; i++) { // such Java
            fragments.add(fments[i]);
        }

        this.pagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.viewpager);
        pager.setAdapter(this.pagerAdapter);
        pager.setOnPageChangeListener(this);
        pager.setCurrentItem(prefs.getDisplayFrag());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.APP_TAG, "MainActivity onResume.");
        if (cache.refreshNeeded()) {
            Provider provider = providerFromPrefIndex();
            cache.launchRefresh(provider);
        }
    }

    public Provider providerFromPrefIndex() {
        String datasource = prefs.getDataSource();
        int providerIdx = 0;
        if (datasource.equals("coinmarketcap")) {
            providerIdx = 0;
        }
        if (datasource.equals("coincapio")) {
            providerIdx = 1;
        }
        Log.d(Constants.APP_TAG, "refreshNeeded. launchRefresh using "
                + datasource + " idx " + providerIdx);
        Provider provider = providers.get(providerIdx);
        return provider;
    }

    @Override
    public void onFragementReady(CoinListFragment frag) {
        Log.d(Constants.APP_TAG, "onFragmentReady. setting " + frag);
        Provider provider = providerFromPrefIndex();
        frag.setDataSourceName(provider.getDisplayName());
    }

    @Override
    public void cacheUpdateDone(List<Coin> coins, Provider provider) {
        Log.d(Constants.APP_TAG, "cacheUpdateDone()");
        if (coins == null) {
        } else {
            this.coins = coins;
            db.add(coins);
            CoinListFragment masterFrag =
                    // (CoinListFragment) getSupportFragmentManager()
                    // .findFragmentById(R.id.masterListFragment);
                    (CoinListFragment) pagerAdapter.getItem(0);
            fragCacheGoodFixup(masterFrag, coins, provider);
            ArrayList<Coin> favCoins = coinListFavFilter(coins);
            CoinListFragment favoritesFrag = (CoinListFragment) pagerAdapter
                    .getItem(1);
            fragCacheGoodFixup(favoritesFrag, favCoins, provider);
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

    public void fragCacheGoodFixup(CoinListFragment fragment, List<Coin> coins,
            Provider provider) {
        fragment.fetchErr("");
        fragment.add(coins);
        fragment.topTime(cache.last);
        fragment.setDataSourceName(provider.getDisplayName());
        fragment.refreshing(false);
    }

    @Override
    public void cacheUpdateStarted() {
        ((CoinListFragment) pagerAdapter.getItem(0)).refreshing(true);
        ((CoinListFragment) pagerAdapter.getItem(1)).refreshing(true);
    }

    @Override
    public void cacheErr(String err) {
        ((CoinListFragment) pagerAdapter.getItem(0)).fetchErr(err);
        ((CoinListFragment) pagerAdapter.getItem(1)).fetchErr(err);
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
        Provider provider = providerFromPrefIndex();
        fragCacheGoodFixup((CoinListFragment) pagerAdapter.getItem(1), favCoins,
                provider);
        return c.favorited;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int fragIdx) {
        prefs.setDisplayFrag(fragIdx);
    }

    @Override
    public void onCoinDetail(Coin coin) {
        Log.d(Constants.APP_TAG, "onCoinDetail " + coin);
        // startActivity(new Intent(this, this));
    }

}
