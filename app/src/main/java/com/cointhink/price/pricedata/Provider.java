package com.cointhink.price.pricedata;

import com.cointhink.price.Coin;
import com.cointhink.price.Database;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public interface Provider {
    List<Coin> parse(String json, Database db);

    Coin coinFormat(JSONObject o) throws JSONException;

    String getDataUrl();

    String getDisplayName();
}
