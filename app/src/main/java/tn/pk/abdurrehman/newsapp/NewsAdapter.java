package tn.pk.abdurrehman.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Abdur on 28-Sep-17.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    private static final String DATE_FORMAT = "MMM dd, yyyy";
    private static Context mContext;

    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
        mContext = context;
    }

    private static String convertDate(String dateString) {
        String date = null;

        try {
            //Parse the string into a date variable
            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateString);

            //Now reformat it using desired display pattern:
            date = new SimpleDateFormat(DATE_FORMAT).format(parsedDate);

        } catch (ParseException e) {
            Log.e(TAG, "getDate: Error parsing date", e);
            e.printStackTrace();
        }

        return date;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View rootView, @NonNull ViewGroup parent) {

        if (rootView == null) {
            rootView = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleTextView = (TextView) rootView.findViewById(R.id.news_title_text_view);
        titleTextView.setText(currentNews.getTitle());

        TextView dateTextView = (TextView) rootView.findViewById(R.id.news_date_text_view);
        dateTextView.setText(convertDate(currentNews.getDate()));

        TextView sectionTextView = (TextView) rootView.findViewById(R.id.news_section_text_view);
        sectionTextView.setText(currentNews.getSection());

        return rootView;
    }
}
