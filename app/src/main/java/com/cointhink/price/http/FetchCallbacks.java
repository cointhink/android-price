package com.cointhink.price.http;

public interface FetchCallbacks {
    void bytesFetched(Response response);

    void progressUpdate(String s);
}
