package com.injectorx86.newsfeed.models;

public class News {

    private String mThumbnailURL;
    private String mTitle;
    private String mReporter;
    private String mDate;
    private String mDescription;
    private String mArticleURL;

    public News(String thumbnailURL, String title, String reporter, String date, String description, String articleURL) {

        this.mThumbnailURL = thumbnailURL;
        this.mTitle = title;
        this.mDate = date.replace("T", " ").replace("Z", "");
        this.mReporter = reporter;
        this.mDescription = description;
        this.mArticleURL = articleURL;

        if(this.mThumbnailURL.equals("null")) {
            this.mThumbnailURL = null;
        }
        else if(this.mThumbnailURL.startsWith("//") && !this.mThumbnailURL.contains("http")) {
            this.mThumbnailURL = "https:" + this.mThumbnailURL;
        }
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReporter() { return mReporter.equals("null") ? "Anonymous" : mReporter; }

    public String getDate() { return mDate; }

    public String getDescription() {
        return mDescription;
    }

    public String getArticleURL() { return mArticleURL; }

    @Override
    public String toString() {
        return getTitle()
                + "\n" + getDate()
                + "\n" + getReporter()
                + "\n" + getDescription()
                + "\n" + getArticleURL();
    }
}
