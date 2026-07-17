package com.cointhink.price.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cointhink.price.Coin;
import com.cointhink.price.CoinMasterAdapter;
import com.cointhink.price.Constants;
import com.cointhink.price.FavoriteHandler;
import com.cointhink.price.IconMgr;
import com.cointhink.price.R;
import com.cointhink.price.StarClick;

import java.util.List;

public class CoinMasterListFragment extends CoinListFragment implements StarClick {

    FavoriteHandler favHand;

    ImageView favStar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.list_all_fragment, container,
                false);
        SwipeRefreshLayout swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeView.setOnRefreshListener(() -> {
            Log.d(Constants.APP_TAG, "REFRESH master!");
            swipeView.setRefreshing(false);
        });
        listView = (ListView) view.findViewById(R.id.coinAllList);
        topTextName = (TextView) view.findViewById(R.id.toptext);
        topTextTime = (TextView) view.findViewById(R.id.toptime);
        topTextCount = (TextView) view.findViewById(R.id.topcount);
        favHand = (FavoriteHandler) getActivity();
        iconMgr = new IconMgr(getActivity(), this);
        adapter = new CoinMasterAdapter(this.getActivity(), coinList, iconMgr, this);
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

    @Override
    public void click(Coin c, View v) {
        Log.d(Constants.APP_TAG, "masterListFragment favStar click " + c.symbol);
        boolean newFav = favHand.favoriteToggle(c);
        adapter.viewFreshed(v, c);
    }

    @Override
    public void nameClick(Coin coin, View convertView) {
        mListener.onCoinDetail(coin);
    }

}
