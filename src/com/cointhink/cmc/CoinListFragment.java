package com.cointhink.cmc;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CoinListFragment extends Fragment {
    public List<Coin> coinList;
    public IconMgr iconMgr;

    private ListView listView;
    protected Date topTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_all_fragment, container,
                false);
        listView = (ListView) view.findViewById(R.id.coinAllList);
        return view;
    }

}
