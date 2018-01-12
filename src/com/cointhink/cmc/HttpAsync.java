package com.cointhink.cmc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class HttpAsync extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private FetchCallbacks fetcher;

    public HttpAsync(FetchCallbacks fetcher) {
        super();
        this.fetcher = fetcher;
    }

    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String result;
        String inputLine;

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

           //Create a new InputStreamReader
           InputStreamReader streamReader = new
               InputStreamReader(connection.getInputStream());

           //Create a new buffered reader and String Builder
           BufferedReader reader = new BufferedReader(streamReader);
           StringBuilder stringBuilder = new StringBuilder();

           //Check if the line we are reading is not null
           while((inputLine = reader.readLine()) != null){
              stringBuilder.append(inputLine);
           }

           //Close our InputStream and Buffered reader
           reader.close();
           streamReader.close();

           //Set our result equal to our stringBuilder
           result = stringBuilder.toString();
        }
        catch(IOException e){
           e.printStackTrace();
           result = null;
        }

        return result;
     }

    // runs on the UI thread!
    @Override
    protected void onPostExecute(String result){
       super.onPostExecute(result);
       Log.d(Constants.APP_TAG, "Background fetch done. result len "+result.length());
       fetcher.stringFetched(result);
    }
 }

