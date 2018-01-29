package com.cointhink.cmc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Prefs {
    private final SharedPreferences sharedPrefs;
    private String KEY_CONFIGURED = "configured";

    public Prefs(Context ctx) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        finishConstruct();
    }

    private void finishConstruct() {
        if (sharedPrefs.getBoolean(KEY_CONFIGURED, false) == false) {
            ensureDefaults();
        }
    }

    public String getDataSource() {
        return sharedPrefs.getString(Constants.PREFERENCE_DATA_SOURCE, "none");
    }

    private void ensureDefaults() {
        /* set default user preferences */
        Editor editor = sharedPrefs.edit();
        /* Data Source: true */
        editor.putString(Constants.PREFERENCE_DATA_SOURCE, "coinmarketcap");
        editor.commit();
    }
 }