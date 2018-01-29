package com.cointhink.cmc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CoinFavoritesFragment extends CoinListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.favorites_fragment, container,
                false);
        listView = (ListView) view.findViewById(R.id.coinFavList);
        topTextName = (TextView) view.findViewById(R.id.topFavtext);
        topTextTime = (TextView) view.findViewById(R.id.topFavtime);
        topTextCount = (TextView) view.findViewById(R.id.topFavcount);
        iconMgr = new IconMgr(getActivity(), this);
        adapter = new CoinFavoritesAdapter(this.getActivity(), coinList,
                iconMgr);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        topTimeFreshen();
        countFreshen();
        refreshing(this.refreshing);
    }

    @Override
    public void topTimeFreshen() {
        String count = adapter == null ? "?" : ""+adapter.getCount();
        topTime(count + " favorites@" + timeStr());
    }
}
