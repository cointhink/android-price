package com.cointhink.price;

import com.cointhink.price.ui.CoinListFragment;

public interface FragmentReadyListener {
    public abstract void onFragementReady(CoinListFragment fragment);

    public abstract void onCoinDetail(Coin coin);
}
