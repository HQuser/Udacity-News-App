package tn.pk.abdurrehman.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Abdur on 27-Sep-17.
 * This class provides utility methods related to
 * downloading and parsing the xml
 * and doing various other tasks
 */

public class QueryUtils {

    /**
     * Purpose of this method is to create a return a URL object
     * from the URL string passed
     *
     * @param rawUrl is the url string provided
     * @return the URL object from url string
     */
    public static URL createURL(String rawUrl) {
        URL url = null;

        try {
            url = new URL(rawUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "createURL: Error creating a URL object", e);
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method creates a HttpURLConnection from URL Object
     *
     * @param url is the URL object to create connection from
     * @return the InputStream from the HttpURLConnection
     */
    public static InputStream createHttpURLConnection(URL url) {
        InputStream inputStream = null;

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
            } else {
                Log.e(TAG, "createHttpURLConnection: response code unsuccessful"
                        + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "createHttpURLConnection: Error creating InputStream", e);
            e.printStackTrace();
        }

        return inputStream;
    }

    /**
     * This method extracts the JSON String from the InputStream
     *
     * @param inputStream is the stream containing the JSON
     * @return String of JSON
     */
    public static String getJSONFromInputStream(InputStream inputStream) {

        StringBuilder json = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            String line = bufferedReader.readLine();

            while (line != null) {
                json.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.e(TAG, "getJSONFromInputStream: Error reading data from InputStream", e);
            e.printStackTrace();
        }

        return json.toString();
    }

    /**
     * This method extracts the fields required to instantiate the News Object
     * from the JSON string passed to this method
     *
     * @param jsonStr is the JSON string
     * @return List of News Objects
     */
    public static List<News> getNewsListFromJSON(String jsonStr) {
        List<News> newsList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonStr);
            JSONObject response = root.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentNews = resultsArray.getJSONObject(i);

                String title = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String date = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");

                newsList.add(new News(title, url, section, date));
            }
        } catch (JSONException e) {
            Log.e(TAG, "getNewsListFromJSON: Error processing JSON from string", e);
            e.printStackTrace();
        }

        return newsList;
    }
}
