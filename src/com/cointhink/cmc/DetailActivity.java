package com.cointhink.cmc;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.cointhink.cmc.http.FetchCallbacks;
import com.cointhink.cmc.http.Response;
import com.cointhink.cmc.pricedata.Provider;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity
        implements CacheCallbacks, IconCallback {
    private Prefs prefs;
    private Database db;
    private Cache cache;
    private IconMgr iconMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String json = getIntent().getExtras().getString("COIN_JSON");
        Coin coin = Coin.fromJson(json);
        Log.d(Constants.APP_TAG, "DetailActivity onCreate bundle: "
                + ((savedInstanceState == null) ? "" : "true") + coin.symbol);

        prefs = new Prefs(this);
        db = new Database(getApplicationContext()).open();
        cache = new Cache(this, db);
        iconMgr = new IconMgr(this, this);

        setContentView(R.layout.detail_activity);
        uiFreshen(coin);
        redditFreshen("/r/btc");
    }

    private void redditFreshen(String subreddit) {
        Net.redditGet(subreddit, new OnFetched());
    }

    class OnFetched implements FetchCallbacks {
        @Override
        public void bytesFetched(Response response) {
            Log.d(Constants.APP_TAG, "DetailActivity bytesFetched "+response);
            String json;
            try {
                if (response.data != null) {
                    json = new String(response.data, "UTF-8");
                    Log.d(Constants.APP_TAG, "DetailActivity bytesFetched: "+json.length());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void progressUpdate(Integer i) {
        }
    }

    public void uiFreshen(Coin coin) {
        ImageView iconView = (ImageView) findViewById(R.id.detail_coinIcon);
        Bitmap icon = iconMgr.loadOrFetch(coin, coin.img_url);
        if (icon == null) {
            iconView.setImageResource(R.drawable.icon_blank);
        } else {
            iconView.setImageBitmap(icon);
        }
        TextView symbol = ((TextView) findViewById(R.id.detail_coinSymbol));
        symbol.setText(coin.symbol);
        TextView name = ((TextView) findViewById(R.id.detail_coinName));
        name.setText(coin.name);
        TextView price = ((TextView) findViewById(R.id.detail_coinPrice));
        price.setText(CoinAdapter.priceMangle(coin.price));
        TextView vol24 = ((TextView) findViewById(R.id.detail_coinVolume));
        vol24.setText(CoinAdapter.capParse(coin.vol_24h));
        vol24.setTextColor(
                CoinAdapter.floatToColor(Float.parseFloat(coin.chg_24h)));
    }

    @Override
    public void cacheUpdateStarted() {
    }

    @Override
    public void cacheUpdateDone(List<Coin> coins, Provider provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void iconReady(Coin coin, Bitmap bitmap) {
        uiFreshen(coin);
    }

    @Override
    public void cacheErr(String err) {
        // TODO Auto-generated method stub

    }
}
