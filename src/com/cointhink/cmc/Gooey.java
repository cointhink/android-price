package com.cointhink.cmc;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Gooey {

    private ArrayAdapter<Coin> adapter;
    private TextView topTextName;
    private TextView topTextTime;
    private ListView listView;

    public Gooey(Activity ctx, List<Coin> coinList) {
        listView = (ListView) ctx.findViewById(R.id.coinAllList);
        topTextName = (TextView) ctx.findViewById(R.id.toptext);
        topTextTime = (TextView) ctx.findViewById(R.id.toptime);
        adapter = new CoinAdapter(ctx, coinList);
        listView.setAdapter(adapter);
        topTextName.setText("CoinMarketCap");
    }

    public void topTime(Date time) {
        topTextTime.setText(timeFmt(time));
    }

    private String timeFmt(Date time) {
        return time.getHours() + ":" + time.getMinutes();
    }

    public void add(List<Coin> coins) {
        adapter.addAll(coins);
    }

}
