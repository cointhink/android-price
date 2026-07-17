package com.cointhink.price.http;

import com.cointhink.price.Coin;

public class CoinResponse extends Response {
    public Coin coin;

    public CoinResponse(Coin coin, byte[] data) {
        this.coin = coin;
        this.data = data;
    }
}
