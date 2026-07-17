package com.cointhink.price;

import com.cointhink.price.pricedata.Provider;

import java.util.List;

public interface CacheCallbacks {
    public void cacheUpdateStarted();

    public void cacheUpdateDone(List<Coin> coins, Provider provider);

    public void cacheErr(String err);
}
