package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Cache cache = new Cache();
    private ListView listView;
    private List<Coin> coinList = new ArrayList<>();
    private ArrayAdapter<Coin> adapter;
    private TextView topTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.coinAllList);
        topTextView = (TextView)findViewById(R.id.toptext);
        adapter = new CoinAdapter(this, coinList);
        listView.setAdapter(adapter);
        topTextView.setText("CoinMarketCap");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.APP_TAG, "onResume");

        if (cache.refreshNeeded()) {
            Log.d(Constants.APP_TAG, "refreshNeeded");
            List<Coin> coins = cache.launchRefresh();
            adapter.addAll(coins);
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
}
