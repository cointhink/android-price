package com.cointhink.cmc.ui;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.cointhink.cmc.Constants;
import com.cointhink.cmc.MainActivity;
import com.cointhink.cmc.R;

public class PrefsFragment extends PreferenceFragmentCompat
        implements OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPrefs;
    private final String[] keys = {Constants.PREFERENCE_DATA_SOURCE, Constants.PREFERENCE_PROVIDER_KEY,
            Constants.PREFERENCE_VERSION};

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
        addMyResetListener();
        Preference preference = getPreferenceScreen().findPreference(Constants.PREFERENCE_DATA_SOURCE);
        preference.setOnPreferenceClickListener(preference1 -> {
            Log.d(Constants.APP_TAG, "onprefclick for data_source pref");
            return true;
        });
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

        if (key.equals(Constants.PREFERENCE_DATA_SOURCE)) {
            summary = ((MainActivity) getActivity()).prefDataSourceName();
        }
        if (key.equals(Constants.PREFERENCE_PROVIDER_KEY)) {
            String provider_key = sharedPrefs.getString(Constants.PREFERENCE_PROVIDER_KEY, "");
            if (provider_key.length() > 0) {
                String key_frag = provider_key.substring(0, 4);
                summary = key_frag + "...<key recorded>";
            } else {
                summary = "-missing key-";
            }
        }
        Log.d(Constants.APP_TAG, "prefSummary for " + key + " is " + summary);
        preference.setSummary(summary);
    }

    private void addMyResetListener() {
        Preference reset = findPreference(Constants.PREFERENCE_BEER);
        reset.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference pref) {
                ((MainActivity) getActivity()).buy();
                return true;
            }
        });
    }
}