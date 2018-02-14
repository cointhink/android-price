package com.cointhink.cmc.http;

public interface FetchCallbacks {
    void bytesFetched(Response response);
    void progressUpdate(Integer i);
}
