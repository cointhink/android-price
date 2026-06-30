package com.cointhink.cmc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryProductDetailsResult;
import com.android.billingclient.api.UnfetchedProduct;
import com.cointhink.cmc.pricedata.CoinCapIo;
import com.cointhink.cmc.pricedata.CoinMarketCap;
import com.cointhink.cmc.pricedata.Provider;
import com.cointhink.cmc.ui.CoinDetail;
import com.cointhink.cmc.ui.CoinFavoritesFragment;
import com.cointhink.cmc.ui.CoinListFragment;
import com.cointhink.cmc.ui.CoinMasterListFragment;
import com.cointhink.cmc.ui.PrefsFragment;
import com.google.common.collect.ImmutableList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements CacheCallbacks, FavoriteHandler, FragmentReadyListener, OnPageChangeListener {

    private Cache cache;
    private Database db;
    private PagerAdapter pagerAdapter;
    private List<Coin> coins;
    private Prefs prefs;
    private List<Provider> providers;
    private CoinDetail detailFragment;
    private ViewPager pager;

    private BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.APP_TAG, "MainActivity onCreate bundle: " + ((savedInstanceState == null) ? "" : "true") + " pagerAdapter: " + pagerAdapter);
        setContentView(R.layout.mainframe);

        prefs = new Prefs(this);

        db = new Database(getApplicationContext()).open();

        Log.d(Constants.APP_TAG, "MainActivity onCreate db open. coin count " + db.rowCount(Database.TABLE_COINS));
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
    }

    private void setupFragments(Fragment... fments) {
        List<Fragment> fragments = new Vector<>();
        for (int i = 0; i < fments.length; i++) { // such Java
            fragments.add(fments[i]);
        }

        this.pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);

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

        PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                // To be implemented in a later section.
            }
        };

        billingClient = BillingClient.newBuilder(MainActivity.this).setListener(purchasesUpdatedListener)
                // Configure other settings.
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    // It's a good practice to query products after the connection is established.
                    //queryProductDetails();
//                    Toast.makeText(this, "BillingReponse OK", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Billing setup OK", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                // This is automatically handled by the library when you call a method that requires a connection.
            }
        });
        Log.d(Constants.APP_TAG, "InApp Purchase Service requested.");
        Toast.makeText(this, "BillingClient", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        Log.d(Constants.APP_TAG, "refreshNeeded. launchRefresh using " + datasource + " idx " + providerIdx);
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
            CoinListFragment favoritesFrag = (CoinListFragment) pagerAdapter.getItem(1);
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

    public void fragCacheGoodFixup(CoinListFragment fragment, List<Coin> coins, Provider provider) {
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
        fragCacheGoodFixup((CoinListFragment) pagerAdapter.getItem(1), favCoins, provider);
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
        mgr.beginTransaction().replace(R.id.viewpager, detailFragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 3) {
            pager.setCurrentItem(prefs.getDisplayFrag(), false);
        }
    }

    String sku = "cointhinkprice_deluxe";
    final int REQUEST_CODE = 1001;

    public void buy(String selectedOfferToken) {
        QueryProductDetailsParams queryProductDetailsParams = QueryProductDetailsParams.newBuilder().setProductList(ImmutableList.of(QueryProductDetailsParams.Product.newBuilder().setProductId("product_id_example").setProductType(BillingClient.ProductType.SUBS).build())).build();

        billingClient.queryProductDetailsAsync(queryProductDetailsParams, new ProductDetailsResponseListener() {
            public void onProductDetailsResponse(BillingResult billingResult, QueryProductDetailsResult queryProductDetailsResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    for (ProductDetails productDetails : queryProductDetailsResult.getProductDetailsList()) {
                        // Process successfully retrieved product details here.
                        ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList = ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder()
                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                .setProductDetails(productDetails)
                                // Get the offer token:
                                // a. For one-time products, call ProductDetails.getOneTimePurchaseOfferDetailsList()
                                // for a list of offers that are available to the user.
                                // b. For subscriptions, call ProductDetails.getSubscriptionOfferDetails()
                                // for a list of offers that are available to the user.
                                .setOfferToken(selectedOfferToken).build());

                        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList).build();

                        // Launch the billing flow
                        BillingResult billingResult2 = billingClient.launchBillingFlow(MainActivity.this, billingFlowParams);
                    }

                    for (UnfetchedProduct unfetchedProduct : queryProductDetailsResult.getUnfetchedProductList()) {
                        // Handle any unfetched products as appropriate.
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
