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
        ((TextView) convertView.findViewById(R.id.coinName)).setText(coin.name);
        String symbol = coin.symbol;
        if (symbol.length() < 4) {
            symbol = symbol + "_";
        }
        ((TextView) convertView.findViewById(R.id.coinSymbol))
                .setText(symbol);
        ((TextView) convertView.findViewById(R.id.coinPrice))
                .setText(coin.price);

        // Return the completed view to render on screen
        return convertView;
    }
}
