package com.cointhink.cmc;

import java.util.List;

public class Cache {

    public boolean refreshNeeded() {
        return true;
    }

    public List<Coin> launchRefresh() {
        return Net.cmcGet();
    }

}
