package com.cointhink.price.http;

import com.cointhink.price.Coin;

public class CoinRequest extends Request {
    public Coin coin;

    public CoinRequest(Coin coin, String url, String auth) {
        this.coin = coin;
        this.url = url;
        this.auth = auth;
    }
}
