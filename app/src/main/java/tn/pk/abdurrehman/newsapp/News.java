package tn.pk.abdurrehman.newsapp;

/**
 * Created by Abdur on 28-Sep-17.
 */

public class News {
    private String mTitle;
    private String mUrl;
    private String mSection;
    private String mDate;

    public News(String mTitle, String mUrl, String mSection, String mDate) {
        this.mTitle = mTitle;
        this.mUrl = mUrl;
        this.mSection = mSection;
        this.mDate = mDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getSection() {
        return mSection;
    }

    public String getDate() {
        return mDate;
    }
}
