package com.cointhink.cmc.http;

import com.cointhink.cmc.Coin;

public class CoinResponse extends Response {
    public Coin coin;

    public CoinResponse(Coin coin, byte[] data) {
        this.coin = coin;
        this.data = data;
    }
}
