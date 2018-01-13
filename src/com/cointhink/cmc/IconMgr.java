package com.cointhink.cmc;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

public class IconMgr implements FetchCallbacks{
    private File directory;

    public IconMgr(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        // Create imageDir
        directory = cw.getDir("icons", Context.MODE_PRIVATE);
    }

    public boolean hasCoin(String symbol, String url) {
        String coinIconFilename = directory.getAbsolutePath()+"/"+symbol;
        Log.d(Constants.APP_TAG, "icon check: "+coinIconFilename);
        File coinIconFile = new File(coinIconFilename);
        if(coinIconFile.exists()) {
            return true;
        } else {
            // fetch
            Net.cmcGet(url, this);
        }
        return false;
    }

    @Override
    public void bytesFetched(byte[] data) {
        Log.d(Constants.APP_TAG, "icon fetched "+data.length);
    }

}
