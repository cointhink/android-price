package com.cointhink.cmc;

import com.cointhink.cmc.http.CoinRequest;
import com.cointhink.cmc.http.CoinRequestAsync;
import com.cointhink.cmc.http.FetchCallbacks;

public class Net {

    public static void cmcGet(Coin coin, String url, FetchCallbacks cache) {
        // Instantiate new instance of our class
        CoinRequestAsync task = new CoinRequestAsync(cache);
        CoinRequest request = new CoinRequest(coin, url);

        task.execute(request);
    }

    public static void redditGet(String subreddit, FetchCallbacks callback) {
        CoinRequestAsync task = new CoinRequestAsync(callback);

    }
}
