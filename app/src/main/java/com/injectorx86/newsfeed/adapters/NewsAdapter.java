package com.injectorx86.newsfeed.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.injectorx86.newsfeed.models.News;
import com.injectorx86.newsfeed.models.NewsViewHolder;
import com.injectorx86.newsfeed.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private ArrayList<News> mNewsList;
    private Activity mActivity;

    public NewsAdapter(ArrayList<News> newsList, Activity mActivity) {
        this.mNewsList = newsList;
        this.mActivity = mActivity;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = mNewsList.get(position);

        if(news.getThumbnailURL() != null) {
            holder.mNewsThumbnail.setImageDrawable(null);
            Picasso.with(holder.mNewsThumbnail.getContext())
                    .load(news.getThumbnailURL())
                    .fit()
                    .noFade()
                    .into(holder.mNewsThumbnail);
        }

        holder.mNewsTitle.setText(news.getTitle());
        holder.mNewsReporter.setText("By " + news.getReporter());

        holder.mNewsDescription.setText(news.getDescription());
        holder.mNewsDate.setTypeface(null, Typeface.ITALIC);

        holder.mNewsDate.setText(Html.fromHtml(news.getDate() + "&nbsp&nbsp&nbsp&nbsp<b>PRESS TO READ MORE</b>"));

        final String articleURL = news.getArticleURL();
        holder.mNewsDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleURL));
               mActivity.startActivity(browserIntent);
           }
        });

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void update(ArrayList<News> newsList) {
        mNewsList.clear();
        mNewsList.addAll(newsList);
        notifyDataSetChanged();
    }
}
