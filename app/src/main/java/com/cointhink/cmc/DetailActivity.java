package com.cointhink.cmc;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cointhink.cmc.http.FetchCallbacks;
import com.cointhink.cmc.http.Response;
import com.cointhink.cmc.pricedata.Provider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity
        implements CacheCallbacks, IconCallback {
    private Prefs prefs;
    private Database db;
    private Cache cache;
    private IconMgr iconMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String json = getIntent().getExtras().getString("COIN_JSON");
        Coin coin = Coin.fromJson(json);
        Log.d(Constants.APP_TAG, "DetailActivity onCreate bundle: "
                + ((savedInstanceState == null) ? "" : "true") + coin.symbol);

        prefs = new Prefs(this);
        db = new Database(getApplicationContext()).open();
        cache = new Cache(this, db);
        iconMgr = new IconMgr(this, this);

        setContentView(R.layout.detail_activity);
        uiFreshen(coin);
        redditFreshen(coin);
    }

    private void redditFreshen(Coin coin) {
        String subreddit = Reddit.coinToSubreddit(coin);
        Net.redditGet(subreddit, new OnFetched());
    }

    class OnFetched implements FetchCallbacks {
        @Override
        public void bytesFetched(Response response) {
            Log.d(Constants.APP_TAG, "DetailActivity bytesFetched " + response);
            String json;
            try {
                if (response.data != null) {
                    json = new String(response.data, "UTF-8");
                    JSONObject jo = new JSONObject(json);
                    if (jo.has("error")) {
                        Log.d(Constants.APP_TAG, "DetailActivity err: "
                                + jo.getString("message"));
                    } else {
                        JSONArray posts = jo.getJSONObject("data")
                                .getJSONArray("children");
                        Log.d(Constants.APP_TAG,
                                "DetailActivity posts: " + posts.length());
                        List<WebLink> headlines = nonPinned(posts);
                        redditUiFreshen(headlines);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private List<WebLink> nonPinned(JSONArray posts) {
            ArrayList<WebLink> subjects = new ArrayList();
            for (int i = 0, l = posts.length(); i < l; i++) {
                try {
                    JSONObject post = ((JSONObject) posts.get(i))
                            .getJSONObject("data");
                    if (post.getBoolean("pinned") == false
                            && post.getBoolean("stickied") == false) {
                        String permaPermalink = "https://reddit.com"+post.getString("permalink");
                        WebLink webLink = new WebLink(post.getString("title"),
                                permaPermalink);
                        subjects.add(webLink);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return subjects;
        }

        @Override
        public void progressUpdate(Integer i) {
        }
    }

    public void uiFreshen(Coin coin) {
        ImageView iconView = (ImageView) findViewById(R.id.detail_coinIcon);
        Bitmap icon = iconMgr.loadOrFetch(coin, coin.img_url);
        if (icon == null) {
            iconView.setImageResource(R.drawable.icon_blank);
        } else {
            iconView.setImageBitmap(icon);
        }
        TextView symbol = ((TextView) findViewById(R.id.detail_coinSymbol));
        symbol.setText(coin.symbol);
        TextView name = ((TextView) findViewById(R.id.detail_coinName));
        name.setText(coin.name);
        TextView price = ((TextView) findViewById(R.id.detail_coinPrice));
        price.setText(CoinAdapter.priceMangle(coin.price));
        TextView chg24 = ((TextView) findViewById(R.id.detail_coinChange));
        chg24.setText(coin.chg_24h);
        chg24.setTextColor(
                CoinAdapter.floatToColor(Float.parseFloat(coin.chg_24h)));
        TextView vol24 = ((TextView) findViewById(R.id.detail_coinVolume));
        vol24.setText(CoinAdapter.capParse(coin.vol_24h));
        vol24.setTextColor(
                CoinAdapter.floatToColor(Float.parseFloat(coin.vol_24h)));
        TextView sub = ((TextView) findViewById(R.id.detail_coinSubreddit1));
        redditClick(sub, coin);
        sub = ((TextView) findViewById(R.id.detail_coinSubreddit2));
        redditClick(sub, coin);
        sub = ((TextView) findViewById(R.id.detail_coinSubreddit3));
        redditClick(sub, coin);
    }

    private void redditClick(TextView sub, Coin coin) {
        String subReddit = Reddit.coinToSubreddit(coin);
        sub.setText(Html.fromHtml(subReddit));
        final String redditUrl = "https://reddit.com/" + subReddit;
        sub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(redditUrl));
                startActivity(browserIntent);
            }
        });
    }

    public void redditUiFreshen(List<WebLink> headlines) {
        int[] views = new int[] { R.id.detail_coinReddit1,
                R.id.detail_coinReddit2, R.id.detail_coinReddit3 };
        for (int i = 0, l = Math.min(views.length,
                headlines.size()); i < l; i++) {
            TextView headline = ((TextView) findViewById(views[i]));
            if (headline != null) {
                final WebLink webLink = headlines.get(i);
                headline.setText(webLink.headline);
                headline.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(webLink.url));
                        startActivity(browserIntent);
                    }
                });
            } else {
                Log.d(Constants.APP_TAG, "reddit widget missing!");
            }
        }
    }

    @Override
    public void cacheUpdateStarted() {
    }

    @Override
    public void cacheUpdateDone(List<Coin> coins, Provider provider) {
    }

    @Override
    public void iconReady(Coin coin, Bitmap bitmap) {
        uiFreshen(coin);
    }

    @Override
    public void cacheErr(String err) {
    }

    class WebLink {
        public String headline;
        public String url;

        public WebLink(String headline, String url) {
            this.headline = headline;
            this.url = url;
        }
    }
}
