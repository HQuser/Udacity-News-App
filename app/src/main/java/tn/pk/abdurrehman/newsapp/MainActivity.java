package tn.pk.abdurrehman.newsapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?q=android&api-key=test";
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the ListView And Adapter
        ListView listView = (ListView) findViewById(R.id.news_list_view);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);
                QueryUtils.openLinkIntent(currentNews.getUrl(), getApplicationContext());
            }
        });

        new NewsASyncTask().execute(NEWS_REQUEST_URL);

    }

    private class NewsASyncTask extends AsyncTask<String, Void, List<News>> {
        @Override
        protected List<News> doInBackground(String... params) {
            URL url = QueryUtils.createURL(params[0]);
            InputStream inputStream = QueryUtils.createHttpURLConnection(url);
            String json = QueryUtils.getJSONFromInputStream(inputStream);

            return QueryUtils.getNewsListFromJSON(json);
        }

        @Override
        protected void onPostExecute(List<News> newsList) {
            mAdapter.clear();

            if (newsList == null || newsList.isEmpty()) {
                return;
            }

            mAdapter.addAll(newsList);
        }
    }

}
