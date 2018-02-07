package com.cointhink.cmc;

import java.util.List;

import com.cointhink.cmc.pricedata.Provider;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailActivity extends Activity implements CacheCallbacks {
    private Prefs prefs;
    private Database db;
    private Cache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String symbol = getIntent().getExtras().getString("COIN_SYMBOL");
        Log.d(Constants.APP_TAG,
                "DetailActivity onCreate bundle: "
                        + ((savedInstanceState == null) ? "" : "true")
                        + symbol);

        prefs = new Prefs(this);
        db = new Database(getApplicationContext()).open();
        cache = new Cache(this, db);

        setContentView(R.layout.detail_activity);
        TextView title = ((TextView)findViewById(R.id.detail_title));
        title.setText(symbol);
    }

    @Override
    public void cacheUpdateStarted() {
        // TODO Auto-generated method stub

    }

    @Override
    public void cacheUpdateDone(List<Coin> coins, Provider provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void cacheErr(String err) {
        // TODO Auto-generated method stub

    }
}
