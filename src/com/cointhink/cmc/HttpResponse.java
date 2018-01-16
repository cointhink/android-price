package com.cointhink.cmc;

public class HttpResponse {
    public String id;
    public byte[] data;

    public HttpResponse(String id, byte[] data) {
        this.id = id;
        this.data = data;
    }
}
