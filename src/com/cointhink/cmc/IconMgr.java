package com.cointhink.cmc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class IconMgr implements FetchCallbacks {
    private File directory;
    public IconCallback iconCallback;

    public IconMgr(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        // Create imageDir
        directory = cw.getDir("icons", Context.MODE_PRIVATE);
    }

    private String iconPath(String name) {
        return directory.getAbsolutePath() + "/" + name.toLowerCase();
    }

    public Bitmap loadOrFetch(Coin coin, String url) {
        String coinIconFilename = iconPath(coin.symbol);
        File coinIconFile = new File(coinIconFilename);
        if (coinIconFile.exists()) {
            return fileToBitmap(coinIconFilename);
        } else {
            // fetch
            Net.cmcGet(coin, url, this);
        }
        return null;
    }

    public Bitmap fileToBitmap(String coinIconFilename) {
        Bitmap bitmap = BitmapFactory.decodeFile(coinIconFilename);
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 64, 64, true);
        return scaled;
    }

    @Override
    public void bytesFetched(HttpResponse response) {
        if (response.data != null) {
            try {
                String path = iconPath(response.coin.symbol);
                Log.d(Constants.APP_TAG, "icon saved " + response.data.length+"bytes to " + path );
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(response.data);
                fos.close();
                iconCallback.iconReady(response.coin, fileToBitmap(path));
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
