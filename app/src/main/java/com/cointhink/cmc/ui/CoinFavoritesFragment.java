package com.cointhink.cmc.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cointhink.cmc.Coin;
import com.cointhink.cmc.CoinFavoritesAdapter;
import com.cointhink.cmc.Constants;
import com.cointhink.cmc.IconMgr;
import com.cointhink.cmc.R;
import com.cointhink.cmc.StarClick;

public class CoinFavoritesFragment extends CoinListFragment implements StarClick {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.favorites_fragment, container,
                false);
        SwipeRefreshLayout swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeView.setOnRefreshListener(() -> {
            Log.d(Constants.APP_TAG, "REFRESH Favorites!");
            swipeView.setRefreshing(false);
        });
        listView = (ListView) view.findViewById(R.id.coinFavList);
        topTextName = (TextView) view.findViewById(R.id.topFavtext);
        topTextTime = (TextView) view.findViewById(R.id.topFavtime);
        topTextCount = (TextView) view.findViewById(R.id.topFavcount);
        iconMgr = new IconMgr(getActivity(), this);
        adapter = new CoinFavoritesAdapter(this.getActivity(), coinList,
                iconMgr, this);
        listView.setAdapter(adapter);
        mListener.onFragementReady(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        topTimeFreshen();
        refreshing(this.refreshing);
    }

    @Override
    public void topTimeFreshen() {
        String count = adapter == null ? "?" : "" + adapter.getCount();
        topTime(count + " favorites@" + timeStr());
    }

    @Override
    public void click(Coin coin, View convertView) {
    }

    @Override
    public void nameClick(Coin coin, View convertView) {
        mListener.onCoinDetail(coin);
    }
}
