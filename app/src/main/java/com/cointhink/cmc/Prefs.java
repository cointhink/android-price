package com.cointhink.cmc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Prefs {
    private final SharedPreferences sharedPrefs;
    private String KEY_CONFIGURED = "configured";
    private Context context;

    public Prefs(Context ctx) {
        this.context = ctx;
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

        /* Data Source: coinmarketcap */
        editor.putString(Constants.PREFERENCE_DATA_SOURCE, "coincapio");

        /* Data Source: coinmarketcap */
        editor.putInt(Constants.PREFERENCE_DISPLAY_FRAG, 0);

        /* prefs are now configured */
        editor.putBoolean(KEY_CONFIGURED, true);

        editor.commit();
    }

    public void setDisplayFrag(int fragIdx) {
        if (fragIdx <= 1) { // only page 0 or 1
            Editor editor = sharedPrefs.edit();
            editor.putInt(Constants.PREFERENCE_DISPLAY_FRAG, fragIdx);
            editor.commit();
        }
    }

    public int getDisplayFrag() {
        int fragIdx = sharedPrefs.getInt(Constants.PREFERENCE_DISPLAY_FRAG, 0);
        return fragIdx;
    }

    public void versionToast() {
        String version = sharedPrefs.getString(Constants.PREFERENCE_VERSION,
                "none");
        if (!version.equals(Constants.VERSION)) {
            sharedPrefs.edit()
                    .putString(Constants.PREFERENCE_VERSION, Constants.VERSION)
                    .commit();
            Toast.makeText(context,
                    "Cointhink Price upgraded to " + Constants.VERSION,
                    Toast.LENGTH_LONG).show();
        }
    }

    public String providerFullName() {
        String dataSource = getDataSource();
        String[] keys = context.getResources().getStringArray(R.array.ProvidersKeys);
        int idx = 0;
        for(int l=keys.length; idx < l; idx++) {
            if(keys[idx].equals(dataSource)) break;
        }
        return context.getResources().getStringArray(R.array.Providers)[idx];
    }
}