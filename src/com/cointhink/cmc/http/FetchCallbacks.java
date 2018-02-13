package com.cointhink.cmc.http;

import com.cointhink.cmc.http.CoinResponse;

public interface FetchCallbacks {
    void bytesFetched(CoinResponse response);
    void progressUpdate(Integer i);
}
