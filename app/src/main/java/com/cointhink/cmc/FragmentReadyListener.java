package com.cointhink.cmc;

import com.cointhink.cmc.ui.CoinListFragment;

public interface FragmentReadyListener {
    public abstract void onFragementReady(CoinListFragment fragment);

    public abstract void onCoinDetail(Coin coin);
}
