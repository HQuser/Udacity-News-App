package tn.pk.abdurrehman.newsapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<News>> {

    private static final String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?q=android&api-key=test";
    private static final int NEWS_LOADER_ID = 1;
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
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Display message no internet connection found
            mEmptyView.setText(R.string.no_internet);
        }
    }

    // Override the Loader methods
    @Override
    public android.content.Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> newsList /* Data */) {
        mAdapter.clear();

        // If no results
        if (newsList == null || newsList.isEmpty()) {
            mEmptyView.setText(R.string.no_news);
            return;
        }

        // Hide Progress Bar and display results
        mProgressBar.setVisibility(View.GONE);
        mAdapter.addAll(newsList);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        // Reset the adapter to clear news
        mAdapter.clear();
    }
}
