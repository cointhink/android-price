package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CoinListFragment extends Fragment implements IconCallback {
    public List<Coin> coinList = new ArrayList<>();
    public IconMgr iconMgr;

    protected ListView listView;
    protected Date topTime;
    protected TextView topTextName;
    protected TextView topTextTime;
    protected TextView topTextCount;
    protected CoinAdapter adapter;
    public boolean refreshing;
    protected FragmentReadyListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            this.mListener = (FragmentReadyListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FragmentReadyListener");
        }
    }

    protected void topTime(String text) {
        topTextTime.setText(text);
    }

    public void topTime(Date time) {
        this.topTime = time;
        topTimeFreshen();
    }

    public void topTimeFreshen() {
        int count = adapter == null ? 0 : adapter.getCount();
        topTime(count + " coins@" + timeStr());
    }

    public String timeStr() {
        String timeStr;
        if (this.topTime != null) {
            timeStr = timeFmt(this.topTime);
        } else {
            timeStr = "...";
        }
        return timeStr;
    }

    public void setDataSourceName(String name) {
        topTextName.setText(name);
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
        } else {
            Log.d(Constants.APP_TAG,
                    "listView does not have a child at position" + pos + " of "
                            + coinList.size());
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
