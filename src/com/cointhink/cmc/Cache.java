package com.cointhink.cmc;

public class Cache {

    public boolean refreshNeeded() {
        return true;
    }

    public void launchRefresh() {
        Net.cmcGet();
    }

}
