package com.cointhink.cmc;

import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CoinListFragment extends Fragment implements IconCallback {
    public List<Coin> coinList;
    public IconMgr iconMgr;

    protected ListView listView;
    protected Date topTime;
    protected TextView topTextName;
    protected TextView topTextTime;
    protected TextView topTextCount;
    protected CoinAdapter adapter;
    public boolean refreshing;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_all_fragment, container,
                false);
        return view;
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
        int count = adapter == null ? 0 : adapter.getCount();
        topTime(count + " coins@" + timeStr);
    }

    public void countFreshen() {
        topTextName.setText("coinmarketcap.com");
    }

    private String timeFmt(Date time) {
        // Date now = new Date();
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

    public void refreshing(boolean b) {
        this.refreshing = b;
        if (topTextCount != null) {
            if (b) {
                topTextCount.setText("(refreshing)");
            } else {
                topTextCount.setText("");
            }
        }
    }


    @Override
    public void iconReady(Coin coin, Bitmap bitmap) {
        int pos = adapter.getPosition(coin);
        View v = listView.getChildAt(pos - listView.getFirstVisiblePosition());

        if (v != null) {
            adapter.viewFreshed(v, coin);
        }
    }
    public void add(List<Coin> coins) {
        adapter.clear(); // yuk
        adapter.addAll(coins);
    }

    public void fetchErr(String msg) {
        if (topTextCount != null) {
            if (msg.length() > 0) {
                topTextCount.setText("(" + msg + ")");
            } else {
                topTextCount.setText("");
            }
        }
    }
}
