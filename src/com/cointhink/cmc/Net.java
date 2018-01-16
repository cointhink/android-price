package com.cointhink.cmc;

import android.util.Log;

public class Net {

    public static void cmcGet(String id, String url, FetchCallbacks cache) {
        // Instantiate new instance of our class
        HttpAsync task = new HttpAsync(cache);
        HttpRequest request = new HttpRequest(id, url);

        Log.d(Constants.APP_TAG, request.id+": "+request.url);
        task.execute(request);
    }
}
