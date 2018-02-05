package com.cointhink.cmc.ui;

import java.util.List;

import com.cointhink.cmc.Coin;
import com.cointhink.cmc.CoinMasterAdapter;
import com.cointhink.cmc.Constants;
import com.cointhink.cmc.FavoriteHandler;
import com.cointhink.cmc.IconMgr;
import com.cointhink.cmc.R;
import com.cointhink.cmc.StarClick;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CoinMasterListFragment extends CoinListFragment implements StarClick {

    FavoriteHandler favHand;

    ImageView favStar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.list_all_fragment, container,
                false);
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
        Log.d(Constants.APP_TAG, "masterListFragment favStar click "+c.symbol);
        boolean newFav = favHand.favoriteToggle(c);
        adapter.viewFreshed(v, c);
    }

}
