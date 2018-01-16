package com.cointhink.cmc;

public class HttpRequest {
    public Coin coin;
    public String url;

    public HttpRequest(Coin coin, String url) {
        this.coin = coin;
        this.url = url;
    }
}
