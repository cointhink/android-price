package com.cointhink.cmc;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CoinAdapter extends ArrayAdapter<Coin> {
    public CoinAdapter(Context context, List<Coin> coinList) {
        super(context, 0, coinList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Coin coin = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_coin, parent, false);
        }
        // Lookup view for data population
        String capStr = longParse(coin.marketCap);
        ((TextView) convertView.findViewById(R.id.coinCap)).setText(capStr);
        ((TextView) convertView.findViewById(R.id.coinName)).setText(coin.name);
        ((TextView) convertView.findViewById(R.id.coinSymbol))
                .setText(coin.symbol);
        ((TextView) convertView.findViewById(R.id.coinPrice))
                .setText("$"+coin.price);

        // Return the completed view to render on screen
        return convertView;
    }

    public String longParse(String longStr) {
        int decimalPos = longStr.indexOf(".");
        if (decimalPos > 0) {
         longStr = longStr.substring(0, decimalPos);
        }
        long longCap = Long.parseLong(longStr);
        String capStr = "?M";
        if (longCap > 1e6) {
            capStr = (longCap/1000000L)+"M";
        }
        if (longCap > 1e9) {
            capStr = (longCap/1000000000L)+"B";
        }
        return capStr;
    }
}
