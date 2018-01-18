package com.cointhink.cmc;

import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CoinMasterListFragment extends CoinListFragment implements IconCallback {

    private Cache cache;
    private Database db;
    public boolean refreshing;

    private CoinAdapter adapter;
    private TextView topTextName;
    private TextView topTextTime;
    private TextView topTextCount;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.list_all_fragment, container,
                false);
        listView = (ListView) view.findViewById(R.id.coinAllList);
        topTextName = (TextView) view.findViewById(R.id.toptext);
        topTextTime = (TextView) view.findViewById(R.id.toptime);
        topTextCount = (TextView) view.findViewById(R.id.topcount);
        adapter = new CoinAdapter(this.getActivity(), coinList, iconMgr);
        listView.setAdapter(adapter);
        iconMgr.iconCallback = this;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constants.APP_TAG, "coinMasterListFragment onResume");
        topTimeFreshen();
        countFreshen();
        refreshing(this.refreshing);
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
        this.refreshing = b;
        if (topTextCount != null) {
            if (b) {
                topTextCount.setText("(refreshing)");
            } else {
                topTextCount.setText("");
            }
        }
    }

    public void fetchErr(String msg) {
        if (msg.length() > 0) {
            topTextCount.setText("(" + msg + ")");
        } else {
            topTextCount.setText("");
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

}
