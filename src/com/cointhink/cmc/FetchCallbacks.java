package com.cointhink.cmc;

public interface FetchCallbacks {
    void bytesFetched(byte[] json);
    void progressUpdate(Integer i);
}
