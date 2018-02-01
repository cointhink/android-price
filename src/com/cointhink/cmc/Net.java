package com.cointhink.cmc;

public class Net {

    public static void cmcGet(Coin coin, String url, FetchCallbacks cache) {
        // Instantiate new instance of our class
        HttpAsync task = new HttpAsync(cache);
        HttpRequest request = new HttpRequest(coin, url);

        task.execute(request);
    }
}
