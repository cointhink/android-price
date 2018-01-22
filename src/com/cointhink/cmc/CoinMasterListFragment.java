package com.cointhink.cmc;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class CoinMasterListFragment extends CoinListFragment {

    private Cache cache;
    private Database db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_all_fragment, container,
                false);
        listView = (ListView) view.findViewById(R.id.coinAllList);
        topTextName = (TextView) view.findViewById(R.id.toptext);
        topTextTime = (TextView) view.findViewById(R.id.toptime);
        topTextCount = (TextView) view.findViewById(R.id.topcount);
        iconMgr = new IconMgr(getActivity(), this);
        adapter = new CoinMasterAdapter(this.getActivity(), coinList, iconMgr);
        listView.setAdapter(adapter);
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


}
