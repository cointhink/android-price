package com.cointhink.cmc.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.cointhink.cmc.Constants;

import android.os.AsyncTask;
import android.util.Log;

public class CoinRequestTask extends AsyncTask<CoinRequest, Integer, CoinResponse> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private FetchCallbacks fetcher;

    public CoinRequestTask(FetchCallbacks fetcher) {
        super();
        this.fetcher = fetcher;
    }

    @Override
    protected CoinResponse doInBackground(CoinRequest... params) {
        CoinRequest request = params[0];
        byte[] result;

        Log.d(Constants.APP_TAG, "HTTP GET " + request.url);
        HttpURLConnection connection;
        try {
            // Create a URL object holding our url
            URL myUrl = new URL(request.url);

            // Create a connection
            connection = (HttpURLConnection) myUrl.openConnection();

            // Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            // Connect to our url
            connection.connect();
            Log.d(Constants.APP_TAG,
                    "DoInBackground response " + connection.getResponseMessage()
                            + " len " + connection.getContentLength());
            int bufSize = 4096;
            InputStream bis = new BufferedInputStream(
                    connection.getInputStream(), bufSize);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[bufSize];
            int read;
            while (-1 != (read = bis.read(buffer, 0, bufSize))) {

                publishProgress(outputStream.size());
                if (read > 0) {
                    outputStream.write(buffer, 0, read);
                }
            }
            publishProgress(-2);
            result = outputStream.toByteArray();
            // result = IOUtils.toByteArray(connection.getInputStream());

        } catch (IOException e) {
            // e.printStackTrace();
            Log.d(Constants.APP_TAG, "DoInBackground err " + e);
            publishProgress(-1);
            result = null;
        } finally {
            // connection.disconnect();
        }

        return new CoinResponse(request.coin, result);
    }

    // runs on the UI thread
    @Override
    protected void onProgressUpdate(Integer... progress) {
        fetcher.progressUpdate(progress[0]);
    }

    // runs on the UI thread
    @Override
    protected void onPostExecute(CoinResponse response) {
        super.onPostExecute(response);
        if (response.data != null) {
            // Log.d(Constants.APP_TAG,
            // "Background fetch done. result len " + response.data.length);
        } else {
            Log.d(Constants.APP_TAG, "Background fetch done. result NULL ");
        }
        fetcher.bytesFetched(response);
    }
}
