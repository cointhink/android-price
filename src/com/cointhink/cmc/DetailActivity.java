package com.cointhink.cmc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.APP_TAG,
                "DetailActivity onCreate bundle: "
                        + ((savedInstanceState == null) ? "" : "true"));
        setContentView(R.layout.detail_activity);
    }
}
