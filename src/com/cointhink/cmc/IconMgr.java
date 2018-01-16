package com.cointhink.cmc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    private String iconPath(String name) {
        return directory.getAbsolutePath() + "/" + name.toLowerCase();
    }

    public boolean hasCoin(String symbol, String url) {
        String coinIconFilename = iconPath(symbol);
        File coinIconFile = new File(coinIconFilename);
        if (coinIconFile.exists()) {
            Log.d(Constants.APP_TAG,
                    "icon check: " + coinIconFilename + " exists!");
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
                String path = iconPath(response.id);
                Log.d(Constants.APP_TAG, "icon saving to " + path);
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(response.data);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
