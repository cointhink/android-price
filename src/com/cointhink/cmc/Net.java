package com.cointhink.cmc;

import java.util.concurrent.ExecutionException;

import android.util.Log;

public class Net {

    public static String cmcGet() {
        // Some url endpoint that you may have
        String myUrl = "https://api.coinmarketcap.com/v1/ticker/?limit=50";

        // String to place our result in
        String result;

        // Instantiate new instance of our class
        HttpAsync getRequest = new HttpAsync();

        // Perform the doInBackground method, passing in our url
        try {
            Log.d(Constants.APP_TAG, myUrl);
            result = getRequest.execute(myUrl).get();
            Log.d(Constants.APP_TAG, "http done");
            if (result == null) {
                Log.d(Constants.APP_TAG, "NULL response");
            } else {
                return result;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
