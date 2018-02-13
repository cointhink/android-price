package com.cointhink.cmc.http;

import com.cointhink.cmc.Coin;

public class CoinResponse {
    public Coin coin;
    public byte[] data;

    public CoinResponse(Coin coin, byte[] data) {
        this.coin = coin;
        this.data = data;
    }
}
