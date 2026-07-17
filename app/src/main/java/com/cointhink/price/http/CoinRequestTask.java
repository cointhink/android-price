package com.cointhink.price.http;

import android.os.AsyncTask;
import android.util.Log;

import com.cointhink.price.Constants;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CoinRequestTask extends AsyncTask<CoinRequest, String, CoinResponse> {
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

        Log.d(Constants.APP_TAG, String.format("HTTP GET %s auth: %s", request.url, request.auth));
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
            if (request.auth.length() > 0) {
                connection.setRequestProperty("Authorization", "Bearer " + request.auth);
            }

            // Connect to our url
            connection.connect();
            int http_response_code = connection.getResponseCode();
            Log.d(Constants.APP_TAG,
                    String.format("DoInBackground response %d(%s) len %d",
                            http_response_code, connection.getResponseMessage(),
                            connection.getContentLength()));
            if (http_response_code == 401) {
                throw new IOException("API key unrecognized (401)");
            }
            if (http_response_code == 403) {
                throw new IOException("API key access denied (403)");
            }
            if (http_response_code < 200 || http_response_code >= 300) {
                throw new IOException("http " + connection.getResponseCode());
            }

            int bufSize = 4096;
            InputStream bis = new BufferedInputStream(
                    connection.getInputStream(), bufSize);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[bufSize];
            int read;
            while (-1 != (read = bis.read(buffer, 0, bufSize))) {

                publishProgress("" + outputStream.size());
                if (read > 0) {
                    outputStream.write(buffer, 0, read);
                }
            }
            publishProgress("-2wha");
            result = outputStream.toByteArray();

        } catch (IOException e) {
            Log.d(Constants.APP_TAG, "DoInBackground err " + e);
            publishProgress(e.getMessage());
            result = null;
        } finally {
            // connection.disconnect();
        }

        return new CoinResponse(request.coin, result);
    }

    // runs on the UI thread
    @Override
    protected void onProgressUpdate(String... s) {
        fetcher.progressUpdate(s[0]);
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
