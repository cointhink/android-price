package com.cointhink.cmc;

import android.os.AsyncTask;
import android.util.Log;

public class Net {

    public static void cmcGet(String url, FetchCallbacks cache) {
        // String to place our result in
        AsyncTask<String, Void, byte[]> task;

        // Instantiate new instance of our class
        HttpAsync getRequest = new HttpAsync(cache);

        Log.d(Constants.APP_TAG, url);
        task = getRequest.execute(url);
    }
}
