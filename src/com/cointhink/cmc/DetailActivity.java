package com.cointhink.cmc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class DetailActivity extends Activity {
    private Prefs prefs;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.APP_TAG,
                "DetailActivity onCreate bundle: "
                        + ((savedInstanceState == null) ? "" : "true")
                        + getIntent().getExtras().getString("COIN_SYMBOL"));

        prefs = new Prefs(this);
        db = new Database(getApplicationContext()).open();
        setContentView(R.layout.detail_activity);
    }
}
