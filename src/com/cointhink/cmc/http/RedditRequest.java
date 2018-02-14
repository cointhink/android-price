package com.cointhink.cmc.http;

public class RedditRequest extends Request {

    public RedditRequest(String subreddit) {
        this.url = subredditToUrl(subreddit);
    }

    private String subredditToUrl(String subreddit) {
        return "https://www.reddit.com/"+subreddit+".json";
    }

}
