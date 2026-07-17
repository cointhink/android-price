package com.cointhink.price;

import com.cointhink.price.http.CoinRequest;
import com.cointhink.price.http.CoinRequestTask;
import com.cointhink.price.http.FetchCallbacks;
import com.cointhink.price.http.RedditRequest;
import com.cointhink.price.http.RedditRequestTask;

public class Net {

    public static void cmcGet(Coin coin, String url, String api_key, FetchCallbacks cache) {
        // Instantiate new instance of our class
        CoinRequestTask task = new CoinRequestTask(cache);
        CoinRequest request = new CoinRequest(coin, url, api_key);

        task.execute(request);
    }

    public static void redditGet(String subreddit, FetchCallbacks callback) {
        RedditRequestTask task = new RedditRequestTask(callback);
        RedditRequest request = new RedditRequest(subreddit);
        task.execute(request);
    }
}
