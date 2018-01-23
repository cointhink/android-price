package com.cointhink.cmc;

import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;
import android.util.Log;

public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.APP_TAG, "prefFrag onCreate");
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}