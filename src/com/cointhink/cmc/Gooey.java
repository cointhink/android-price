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
    private TextView topTextCount;
    private ListView listView;
    private Date topTime;

    public Gooey(Activity ctx, List<Coin> coinList, IconMgr iconMgr) {
        listView = (ListView) ctx.findViewById(R.id.coinAllList);
        topTextName = (TextView) ctx.findViewById(R.id.toptext);
        topTextTime = (TextView) ctx.findViewById(R.id.toptime);
        topTextCount = (TextView) ctx.findViewById(R.id.topcount);
        adapter = new CoinAdapter(ctx, coinList, iconMgr);
        listView.setAdapter(adapter);
        topTextName.setText("CoinMarketCap");
    }

    private void topTime(String text) {
        topTextTime.setText(text);
    }

    public void topTime(Date time) {
        this.topTime = time;
        topTimeFreshen();
    }

    public void topTimeFreshen() {
        String timeStr;
        if (this.topTime != null) {
            timeStr = timeFmt(this.topTime);
        } else {
            timeStr = "...";
        }
        topTime(adapter.getCount() + " coins@" + timeStr);
    }

    public void countFreshen() {
        topTextName.setText("coinmarketcap.com");
    }

    private String timeFmt(Date time) {
        Date now = new Date();
        // ArrayList<String> words = new ArrayList<>();
        // long duration = now.getTime() - time.getTime();
        // long seconds = duration / 1000;
        // words.add(seconds + " sec");
        //
        // return wordJoin(words, " ");
        // long minutes = duration / 1000 / 60;
        // return "" + minutes + " min";
        int min = time.getMinutes();
        String minPrefix = "";
        if (min < 10) {
            minPrefix = "0";
        }
        return time.getHours() + ":" + minPrefix + time.getMinutes();
    }

    private String wordJoin(List<String> list, String conjunction) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String item : list) {
            if (first)
                first = false;
            else
                sb.append(conjunction);
            sb.append(item);
        }
        return sb.toString();
    }

    public void add(List<Coin> coins) {
        adapter.clear(); // yuk
        adapter.addAll(coins);
    }

    public void refreshing(boolean b) {
        if (b){
            topTextCount.setText("(refreshing)");
        } else {
            topTextCount.setText("");
        }
    }

}
