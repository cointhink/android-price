package com.cointhink.cmc;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.os.AsyncTask;
import android.util.Log;

public class HttpAsync extends AsyncTask<String, Void, byte[]> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private FetchCallbacks fetcher;

    public HttpAsync(FetchCallbacks fetcher) {
        super();
        this.fetcher = fetcher;
    }

    @Override
    protected byte[] doInBackground(String... params){
        String stringUrl = params[0];
        byte[] result;

        Log.d(Constants.APP_TAG, "Background fetching "+stringUrl);
        try {
           //Create a URL object holding our url
           URL myUrl = new URL(stringUrl);

           //Create a connection
           HttpURLConnection connection =(HttpURLConnection)
                  myUrl.openConnection();

           //Set methods and timeouts
           connection.setRequestMethod(REQUEST_METHOD);
           connection.setReadTimeout(READ_TIMEOUT);
           connection.setConnectTimeout(CONNECTION_TIMEOUT);

           //Connect to our url
           connection.connect();
           InputStream is;
            result = IOUtils.toByteArray(connection.getInputStream());
        }
        catch(IOException e){
           e.printStackTrace();
           result = null;
        }

        return result;
     }

    // runs on the UI thread!
    @Override
    protected void onPostExecute(byte[] result){
       super.onPostExecute(result);
       Log.d(Constants.APP_TAG, "Background fetch done. result len "+result.length);
       fetcher.bytesFetched(result);
    }
 }

