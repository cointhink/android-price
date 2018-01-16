package com.cointhink.cmc;

public interface FetchCallbacks {
    void bytesFetched(HttpResponse response);
    void progressUpdate(Integer i);
}
