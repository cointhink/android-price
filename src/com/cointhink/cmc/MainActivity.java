package com.cointhink.cmc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivity extends FragmentActivity {

    private Cache cache;
    private List<Coin> coinList = new ArrayList<>();
    private Gooey gooey;
    private IconMgr iconMgr;
    private Database db;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        if (findViewById(R.id.viewpager) != null) {
            if (savedInstanceState != null) {
                return;
            }
            setupFragments();
        }
    }

    private void setupFragments() {
        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(this, CoinMasterListFragment.class.getName()));
        this.pagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        ViewPager pager = (ViewPager)super.findViewById(R.id.viewpager);
        pager.setAdapter(this.pagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Constants.APP_TAG, "onResume");
    }

}
