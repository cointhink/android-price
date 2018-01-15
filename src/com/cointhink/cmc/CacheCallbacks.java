package com.cointhink.cmc;

import java.util.List;

public interface CacheCallbacks {
    public void cacheUpdateStarted();
    public void cacheUpdateDone(List<Coin> coins);
    public void cacheErr(String err);
}
