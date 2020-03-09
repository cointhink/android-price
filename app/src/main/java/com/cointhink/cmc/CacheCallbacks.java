package com.cointhink.cmc;

import java.util.List;

import com.cointhink.cmc.pricedata.Provider;

public interface CacheCallbacks {
    public void cacheUpdateStarted();
    public void cacheUpdateDone(List<Coin> coins, Provider provider);
    public void cacheErr(String err);
}
