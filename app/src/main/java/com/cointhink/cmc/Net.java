package com.cointhink.cmc;

import com.cointhink.cmc.http.CoinRequest;
import com.cointhink.cmc.http.CoinRequestTask;
import com.cointhink.cmc.http.FetchCallbacks;
import com.cointhink.cmc.http.RedditRequest;
import com.cointhink.cmc.http.RedditRequestTask;

public class Net {

    public static void cmcGet(Coin coin, String url, FetchCallbacks cache) {
        // Instantiate new instance of our class
        CoinRequestTask task = new CoinRequestTask(cache);
        CoinRequest request = new CoinRequest(coin, url);

        task.execute(request);
    }

    public static void redditGet(String subreddit, FetchCallbacks callback) {
        RedditRequestTask task = new RedditRequestTask(callback);
        RedditRequest request = new RedditRequest(subreddit);
        task.execute(request);
    }
}
