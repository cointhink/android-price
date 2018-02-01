package com.cointhink.cmc;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.preference.PreferenceFragment;
import android.util.Log;

public class PrefsFragment extends PreferenceFragment
        implements OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPrefs;
    private final String[] keys = { Constants.PREFERENCE_DATA_SOURCE };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        Log.d(Constants.APP_TAG, "onSharedPreferenceChanged " + key);
        Preference preference = getPreferenceScreen().findPreference(key);
        if (preference != null) {
            refreshSummary(key);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshSummaries();
    }

    protected void refreshSummaries() {
        for (String key : keys) {
            refreshSummary(key);
        }
    }

    public void refreshSummary(String key) {
        Preference preference = getPreferenceScreen().findPreference(key);
        String summary = sharedPrefs.getString(key, "<none>");
        Log.d(Constants.APP_TAG, "prefSummary for " + key + " is " + summary);
        preference.setSummary(summary);
    }
}