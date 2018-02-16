package com.cointhink.cmc;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoinFavoritesAdapter extends CoinAdapter {
    private StarClick starClick;

    public CoinFavoritesAdapter(Context context, List<Coin> coinList,
            IconMgr iconMgr,  StarClick starClick) {
        super(context, coinList, iconMgr);
        this.starClick = starClick;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Coin coin = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_coin_fav, parent, false);
        }
        viewFreshed(convertView, coin);

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void viewFreshed(final View convertView, final Coin coin) {
        // Lookup view for data population
        String capStr = capParse(coin.marketCap);
        ((TextView) convertView.findViewById(R.id.coinCap)).setText(capStr);
        ImageView iconView = (ImageView) convertView
                .findViewById(R.id.coinIcon);
        Bitmap icon = iconMgr.loadOrFetch(coin, coin.img_url);
        if (icon == null) {
            iconView.setImageResource(R.drawable.icon_blank);
        } else {
            iconView.setImageBitmap(icon);
        }
        ((TextView) convertView.findViewById(R.id.coinName)).setText(coin.name);
        ((TextView) convertView.findViewById(R.id.coinSymbol))
                .setText(coin.symbol);
        ((TextView) convertView.findViewById(R.id.coinPrice))
                .setText(priceMangle(coin.price));
        ((TextView) convertView.findViewById(R.id.coinPercentages))
                .setText("24h chg: " + coin.chg_24h + "% vol: $"
                        + capParse(coin.vol_24h));
        Float safeFloat = (float) 0.0; // so much java
        try {
            safeFloat = Float.parseFloat(coin.chg_24h);
        } catch (java.lang.NumberFormatException e) {
        }
        ((TextView) convertView.findViewById(R.id.coinPercentages))
                .setTextColor(floatToColor(safeFloat));
        LinearLayout coinNameBlock = (LinearLayout) convertView.findViewById(R.id.coinNameBlock);
        coinNameBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starClick.nameClick(coin, convertView);
            }
        });
    }

}
