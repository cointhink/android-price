package com.cointhink.cmc;

public class HttpResponse {
    public Coin coin;
    public byte[] data;

    public HttpResponse(Coin coin, byte[] data) {
        this.coin = coin;
        this.data = data;
    }
}
