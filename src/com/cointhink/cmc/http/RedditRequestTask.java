package com.cointhink.cmc.http;

import android.os.AsyncTask;

public class RedditRequestTask  extends AsyncTask<RedditRequest, Integer, RedditResponse> {

    private Object fetcher;

    public RedditRequestTask(FetchCallbacks callback) {
        super();
        this.fetcher = fetcher;
    }

    @Override
    protected RedditResponse doInBackground(RedditRequest... params) {
        // TODO Auto-generated method stub
        return null;
    }

}
