package com.cointhink.cmc;

import java.text.DecimalFormat;
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
        String capStr = capParse(coin.marketCap);
        ((TextView) convertView.findViewById(R.id.coinCap)).setText(capStr);
        ((TextView) convertView.findViewById(R.id.coinName)).setText(coin.name);
        ((TextView) convertView.findViewById(R.id.coinSymbol))
                .setText(coin.symbol);
        ((TextView) convertView.findViewById(R.id.coinPrice))
                .setText("$" + priceMangle(coin.price));

        // Return the completed view to render on screen
        return convertView;
    }

    private String priceMangle(String price) {
        int decimalPos = price.indexOf(".");
        int sigFigInt, sigFigDec;
        String intPart, decPart;
        if (decimalPos == -1) {
            intPart = price;
            decPart = "";
        } else {
            intPart = price.substring(0, decimalPos);
            decPart = price.substring(decimalPos + 1, price.length());
        }
        String mangled = intPart;
        int decSize = Math.max(0, 5 - intPart.length());
        String decMangled = decMassage(decPart, decSize);
        if (decMangled.length() > 0) {
            mangled = mangled + "." + decMangled;
        }
//        Log.d(Constants.APP_TAG, "pricemangle " + price + " intPart:" + intPart
//                + " decPart(" + decSize + "):" + decPart + " => " + mangled);
        return mangled;
    }

    private String decMassage(String decPart, int bestLen) {
        if (bestLen == 1) {
            bestLen = 2; // USD-like
        }
        if (bestLen == 0) {
            decPart = "";
        }
        if (decPart.length() > 0) {
            int padding = bestLen - decPart.length();

            for (int i = 0; i < padding; i++) {
                decPart = decPart + "0";
            }
        }
        if (decPart.length() > bestLen) {
            decPart = decPart.substring(0, bestLen);
        }
        return decPart;
    }

    public String capParse(String longStr) {
        int decimalPos = longStr.indexOf(".");
        if (decimalPos > 0) {
            longStr = longStr.substring(0, decimalPos);
        }
        long longCap = Long.parseLong(longStr);
        double count = longCap / 1000L;
        String unit = "T";
        if (longCap > 1e6) {
            count = (longCap / 1e6);
            unit = "M";
        }
        if (longCap > 1e9) {
            count = (longCap / 1e9);
            unit = "B";
        }
        String countStr;
        if (count < 10) {
            DecimalFormat df = new DecimalFormat("#.#");
            countStr = df.format(count);
            countStr = String.format("%.1f", count);
        } else {
            DecimalFormat df = new DecimalFormat("#");
            countStr = df.format(count);
            countStr = String.format("%.0f", count);
        }
        String capStr = countStr + unit;
        return capStr;
    }
}
