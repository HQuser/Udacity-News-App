package tn.pk.abdurrehman.newsapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?q=android&api-key=test";
    private NewsAdapter mAdapter;
    private TextView mEmptyView;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up empty view
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        // Setting Up Progress Bar
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // Setting up the ListView And Adapter
        ListView listView = (ListView) findViewById(R.id.news_list_view);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        listView.setAdapter(mAdapter);
        listView.setEmptyView(mEmptyView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);
                QueryUtils.openLinkIntent(currentNews.getUrl(), getApplicationContext());
            }
        });


        // If has active internet connection
        if (QueryUtils.hasInternetConnection(getApplicationContext())) {
            // Show progress bar and fetch results
            mProgressBar.setVisibility(View.VISIBLE);
            new NewsASyncTask().execute(NEWS_REQUEST_URL);
        } else {
            // Display message no internet connection found
            mEmptyView.setText(R.string.no_internet);
        }
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
                mEmptyView.setText(R.string.no_news);
                return;
            }

            // Hide Progress Bar and display results
            mProgressBar.setVisibility(View.GONE);
            mAdapter.addAll(newsList);
        }
    }

}
