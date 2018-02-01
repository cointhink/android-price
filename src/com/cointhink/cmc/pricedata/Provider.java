package com.cointhink.cmc.pricedata;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cointhink.cmc.Coin;
import com.cointhink.cmc.Database;

public interface Provider {
    List<Coin> parse(String json, Database db);
    Coin coinFormat(JSONObject o) throws JSONException;
    String getDataUrl();
}
