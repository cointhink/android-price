package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.vending.billing.IInAppBillingService;
import com.cointhink.cmc.pricedata.CoinCapIo;
import com.cointhink.cmc.pricedata.CoinMarketCap;
import com.cointhink.cmc.pricedata.Provider;
import com.cointhink.cmc.ui.CoinDetail;
import com.cointhink.cmc.ui.CoinFavoritesFragment;
import com.cointhink.cmc.ui.CoinListFragment;
import com.cointhink.cmc.ui.CoinMasterListFragment;
import com.cointhink.cmc.ui.PrefsFragment;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CacheCallbacks,
        FavoriteHandler, FragmentReadyListener, OnPageChangeListener {

    private Cache cache;
    private Database db;
    private PagerAdapter pagerAdapter;
    private List<Coin> coins;
    private Prefs prefs;
    private List<Provider> providers;
    private CoinDetail detailFragment;
    private ViewPager pager;
    private IInAppBillingService mService;

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

        prefs.versionToast();

        Intent serviceIntent = new Intent(
                "com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        Log.d(Constants.APP_TAG, "InApp Purchase Service requested.");

    }

    private void setupFragments(Fragment... fments) {
        List<Fragment> fragments = new Vector<>();
        for (int i = 0; i < fments.length; i++) { // such Java
            fragments.add(fments[i]);
        }

        this.pagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                fragments);

        pager = (ViewPager) super.findViewById(R.id.viewpager);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
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
        if (fragIdx == 0 || fragIdx == 1) {
            prefs.setDisplayFrag(fragIdx);
        }
    }

    @Override
    public void onCoinDetail(Coin coin) {
        Log.d(Constants.APP_TAG, "onCoinDetail " + coin);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("COIN_JSON", coin.toJson());
        startActivity(intent);

        // pager.setCurrentItem(3, false);
    }

    public void switchFragment(Fragment frag) {
        FragmentManager mgr = super.getSupportFragmentManager();
        mgr.beginTransaction().replace(R.id.viewpager, detailFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 3) {
            pager.setCurrentItem(prefs.getDisplayFrag(), false);
        }
    }

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            Log.d(Constants.APP_TAG, "InApp Purchase Service is disconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            Log.d(Constants.APP_TAG, "InApp Purchase Service is connected");
        }
    };

    String sku = "cointhinkprice_deluxe";
    final int REQUEST_CODE = 1001;

    public void buy() {
        Bundle buyIntentBundle;
        try {
            if (mService == null) {
                Log.d(Constants.APP_TAG, "InApp Purchase Service is missing");
                Toast.makeText(this, "In-App-Purchase is Unavailable!", Toast.LENGTH_SHORT).show();
            } else {
                buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                        sku, "inapp", null);
                PendingIntent pendingIntent = buyIntentBundle
                        .getParcelable("BUY_INTENT");
                startIntentSenderForResult(pendingIntent.getIntentSender(),
                        REQUEST_CODE, new Intent(), Integer.valueOf(0),
                        Integer.valueOf(0), Integer.valueOf(0));
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SendIntentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == REQUEST_CODE) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    Log.d(Constants.APP_TAG, "PURCHASE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String prefDataSourceName() {
        return prefs.providerFullName();
    }
}
