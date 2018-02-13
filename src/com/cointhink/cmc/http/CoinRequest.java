package com.cointhink.cmc.http;

import com.cointhink.cmc.Coin;

public class CoinRequest {
    public Coin coin;
    public String url;

    public CoinRequest(Coin coin, String url) {
        this.coin = coin;
        this.url = url;
    }
}
