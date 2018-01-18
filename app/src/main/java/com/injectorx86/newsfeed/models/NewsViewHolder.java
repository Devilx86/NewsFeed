package com.injectorx86.newsfeed.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.injectorx86.newsfeed.R;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    public ImageView mNewsThumbnail;
    public TextView mNewsTitle;
    public TextView mNewsReporter;
    public TextView mNewsDescription;
    public TextView mNewsDate;

    public NewsViewHolder(View view) {
        super(view);
        mNewsThumbnail = view.findViewById(R.id.news_thumbnail);
        mNewsTitle = view.findViewById(R.id.news_title);
        mNewsReporter = view.findViewById(R.id.news_reporter);
        mNewsDescription = view.findViewById(R.id.news_description);
        mNewsDate = view.findViewById(R.id.news_date);
    }
}