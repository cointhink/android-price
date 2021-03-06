package com.cointhink.cmc;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ArrayAdapter;

public abstract class CoinAdapter extends ArrayAdapter<Coin> {
    IconMgr iconMgr;

    public CoinAdapter(Context context, List<Coin> coinList, IconMgr iconMgr) {
        super(context, 0, coinList);
        this.iconMgr = iconMgr;
    }

    public static String priceMangle(String price) {
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
        if (intPart.length() > 3) {
            int ilen = intPart.length();
            intPart = intPart.substring(0, ilen - 3) + ","
                    + intPart.substring(ilen - 3, ilen);
        }
        String mangled = intPart;
        int decSize = Math.max(0, 5 - intPart.length());
        String decMangled = decMassage(decPart, decSize);
        if (decMangled.length() > 0) {
            mangled = mangled + "." + decMangled;
        }
        // Log.d(Constants.APP_TAG, "pricemangle " + price + " intPart:" +
        // intPart
        // + " decPart(" + decSize + "):" + decPart + " => " + mangled);
        return mangled;
    }

    public void notifyItemChanged(int pos) {

    }

    protected static String decMassage(String decPart, int bestLen) {
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

    public static String capParse(String longStr) {
        int decimalPos = longStr.indexOf(".");
        if (decimalPos > 0) {
            longStr = longStr.substring(0, decimalPos);
        }
        try {
        long longCap = Long.parseLong(longStr);
        double count = longCap;
        String unit = "";
        if (longCap > 1e3) {
            count = (longCap / 1e3);
            unit = "K";
        }
        if (longCap > 1e6) {
            count = (longCap / 1e6);
            unit = "M";
        }
        if (longCap > 1e9) {
            count = (longCap / 1e9);
            unit = "B";
        }
        if (longCap > 1e12) {
            count = (longCap / 1e12);
            unit = "T"; // Trillion, not thousands
        }
        String countStr;
        if (count < 10) {
            countStr = String.format("%.1f", count);
        } else {
            countStr = String.format("%.0f", count);
        }
        String capStr = countStr + unit;
        return capStr;
        } catch (java.lang.NumberFormatException e) {
            return "???";
        }
    }

    public static int floatToColor(float f) {
        if (f < 0) {
            return Color.RED;
        } else {
            return Color.argb(255, 0, 155, 0);
        }
    }

    public abstract void viewFreshed(View convertView, Coin coin);
}
