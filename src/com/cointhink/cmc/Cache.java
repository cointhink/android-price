package com.cointhink.cmc;

import java.util.Date;
import java.util.List;

public class Cache {

    Date last;

    public boolean refreshNeeded() {
        return true;
    }

    public List<Coin> launchRefresh() {
        String json = Net.cmcGet();
        if (json != null) {
            last = new Date();
            return CoinMarketCap.parse(json);
        }
        return null; // such java
    }

}
