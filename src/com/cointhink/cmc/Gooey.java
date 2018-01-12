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
    private Date topTime;

    public Gooey(Activity ctx, List<Coin> coinList) {
        listView = (ListView) ctx.findViewById(R.id.coinAllList);
        topTextName = (TextView) ctx.findViewById(R.id.toptext);
        topTextTime = (TextView) ctx.findViewById(R.id.toptime);
        adapter = new CoinAdapter(ctx, coinList);
        listView.setAdapter(adapter);
        topTextName.setText("CoinMarketCap");
    }

    public void topTime(String text) {
        topTextTime.setText(text);
    }

    public void topTime(Date time) {
        this.topTime = time;
        topTimeFreshen();
    }

    public void topTimeFreshen() {
        if (this.topTime != null) {
            topTime(timeFmt(this.topTime));
        }
    }

    private String timeFmt(Date time) {
        Date now = new Date();
//        ArrayList<String> words = new ArrayList<>();
//        long duration = now.getTime() - time.getTime();
//        long seconds = duration / 1000;
//        words.add(seconds + " sec");
//
//        return wordJoin(words, " ");
        // long minutes = duration / 1000 / 60;
        // return "" + minutes + " min";
         return time.getHours() + ":" + time.getMinutes();
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
        adapter.addAll(coins);
    }

}
