package com.cointhink.cmc;

public class Reddit {
    public static String coinToSubreddit(Coin coin) {
        String subreddit = coin.name.replaceAll(" ", "");
        if(coin.symbol.equalsIgnoreCase("bch")) {
            subreddit = coin.symbol;
        }
        if(coin.symbol.equalsIgnoreCase("bnb")) {
            subreddit = firstWord(coin.name);
        }
        return "r/" + subreddit.toLowerCase();
    }

    private static String firstWord(String name) {
        return name.split(" ")[0] ;
    }
}
