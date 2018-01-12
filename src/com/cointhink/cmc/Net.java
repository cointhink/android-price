package com.cointhink.cmc;

import android.os.AsyncTask;
import android.util.Log;

public class Net {

    public static String cmcGet(FetchCallbacks cache) {
        // Some url endpoint that you may have
        String myUrl = "https://api.coinmarketcap.com/v1/ticker/?limit=50";

        // String to place our result in
        AsyncTask<String, Void, String> task;

        // Instantiate new instance of our class
        HttpAsync getRequest = new HttpAsync(cache);

        Log.d(Constants.APP_TAG, myUrl);
        task = getRequest.execute(myUrl);
        return null;
    }
}
