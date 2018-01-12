package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements CacheCallbacks {

    private Cache cache;
    private List<Coin> coinList = new ArrayList<>();
    private Gooey gooey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gooey = new Gooey(this, coinList);
        cache = new Cache(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.APP_TAG, "onResume");
        gooey.topTimeFreshen();

        if (cache.refreshNeeded()) {
            Log.d(Constants.APP_TAG, "refreshNeeded. launchRefresh.");
            cache.launchRefresh();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void cacheUpdateDone(List<Coin> coins) {
        Log.d(Constants.APP_TAG, "cacheUpdateDone()");
        if (coins != null) {
            gooey.add(coins);
            gooey.topTime(cache.last);
            //gooey.topTime("loaded");
        }
    }

    @Override
    public void cacheUpdateStarted() {
        Log.d(Constants.APP_TAG, "cacheUpdateStarted()");
        gooey.topTime("-refreshing-");
    }
}
