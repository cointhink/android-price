package com.cointhink.cmc;

import android.util.Log;

public class Net {

    public static void cmcGet(String url, FetchCallbacks cache) {
        // Instantiate new instance of our class
        HttpAsync task = new HttpAsync(cache);

        Log.d(Constants.APP_TAG, url);
        task.execute(url);
    }
}
