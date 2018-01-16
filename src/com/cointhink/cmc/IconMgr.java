package com.cointhink.cmc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

public class IconMgr implements FetchCallbacks {
    private File directory;

    public IconMgr(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        // Create imageDir
        directory = cw.getDir("icons", Context.MODE_PRIVATE);
    }

    public boolean hasCoin(String symbol, String url) {
        String coinIconFilename = directory.getAbsolutePath() + "/" + symbol;
        Log.d(Constants.APP_TAG, "icon check: " + coinIconFilename);
        File coinIconFile = new File(coinIconFilename);
        if (coinIconFile.exists()) {
            return true;
        } else {
            // fetch
            Net.cmcGet(symbol, url, this);
        }
        return false;
    }

    @Override
    public void bytesFetched(HttpResponse response) {
        if (response.data != null) {
            Log.d(Constants.APP_TAG, "icon fetched " + response.data.length);
            try {
                FileOutputStream fos = new FileOutputStream(response.id);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(Constants.APP_TAG, "icon fetched failed. NULL.");

        }
    }

    @Override
    public void progressUpdate(Integer i) {
    }

}
