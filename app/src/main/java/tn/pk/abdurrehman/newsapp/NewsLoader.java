package tn.pk.abdurrehman.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Abdur on 28-Sep-17.
 * This Class loads the news from the url string provided.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private static String newsRequestUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        newsRequestUrl = url;
    }

    @Override
    public List<News> loadInBackground() {
        URL url = QueryUtils.createURL(newsRequestUrl);
        InputStream inputStream = QueryUtils.createHttpURLConnection(url);
        String json = QueryUtils.getJSONFromInputStream(inputStream);

        return QueryUtils.getNewsListFromJSON(json);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
