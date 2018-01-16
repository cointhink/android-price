package com.cointhink.cmc;

import android.util.Log;

public class Net {

    public static void cmcGet(Coin coin, String url, FetchCallbacks cache) {
        // Instantiate new instance of our class
        HttpAsync task = new HttpAsync(cache);
        HttpRequest request = new HttpRequest(coin, url);

        String symbol = request.coin == null ? "" : request.coin.symbol;
        Log.d(Constants.APP_TAG, symbol+": "+request.url);
        task.execute(request);
    }
}
