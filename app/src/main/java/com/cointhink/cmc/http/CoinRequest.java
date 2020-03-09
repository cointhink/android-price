package com.cointhink.cmc.http;

import com.cointhink.cmc.Coin;

public class CoinRequest extends Request {
    public Coin coin;

    public CoinRequest(Coin coin, String url) {
        this.coin = coin;
        this.url = url;
    }
}
