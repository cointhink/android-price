package com.cointhink.cmc.ui;

import com.cointhink.cmc.Constants;
import com.cointhink.cmc.R;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CoinDetail extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(Constants.APP_TAG, "CoinDetail.onCreateView container "+container);
        View view = inflater.inflate(R.layout.detail_activity, container,
                false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Constants.APP_TAG, "CoinDetail.onResume");
    }
}
