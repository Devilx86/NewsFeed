package com.injectorx86.newsfeed.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.injectorx86.newsfeed.MainActivity;
import com.injectorx86.newsfeed.adapters.NewsAdapter;
import com.injectorx86.newsfeed.models.News;
import com.injectorx86.newsfeed.tasks.JSONRequestTask;
import com.injectorx86.newsfeed.AppPreferenceActivity;
import com.injectorx86.newsfeed.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFeedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private ArrayList<News> mNewsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_news_feed, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewsList = new ArrayList<>();
        new DownloadNewsTask(this.getContext(), "Downloading News").execute();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actionbar_items, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.actionbar_refresh:
                new DownloadNewsTask(this.getContext(), "Downloading News").execute();
                return true;

            case R.id.actionbar_preferences:
                Intent intent = new Intent(getActivity(), AppPreferenceActivity.class);
                getActivity().startActivity(intent);
                MainActivity.changeFragment(this.getFragmentManager(), new SettingsFragment());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new DownloadNewsTask(this.getContext(), "Downloading News").execute();
    }

    private void updateUI() {
        if(mNewsAdapter == null) {
            mNewsAdapter = new NewsAdapter(mNewsList, getActivity());
            mRecyclerView.setAdapter(mNewsAdapter);
        } else {
            mNewsAdapter.update(mNewsList);
        }
    }

    private void parseNewsArticles(String jsonString) {
        try {
            mNewsList = new ArrayList<>();
            JSONArray articles = new JSONObject(jsonString).getJSONArray("articles");

            // looping through All articles
            for (int i = 0; i < articles.length(); i++) {
                JSONObject newsElement = articles.getJSONObject(i);

                if(newsElement.getString("title") != null) {
                    News news = new News(
                            newsElement.getString("urlToImage"),
                            newsElement.getString("title"),
                            newsElement.getString("author"),
                            newsElement.getString("publishedAt"),
                            newsElement.getString("description"),
                            newsElement.getString("url"));

                    if(!mNewsList.contains(news))
                        mNewsList.add(news);
                }
            }
        } catch (JSONException e) {
            Log.e("[JSON Exception]", jsonString);
            e.printStackTrace();
        }
    }


    private class DownloadNewsTask extends JSONRequestTask {
        private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        public DownloadNewsTask(Context context, String message) {
            super(context, message);
        }

        @Override
        protected void saveJsonResponse(String jsonResponse) {
            preferences.edit().putString("NewsJsonCache", jsonResponse).apply();
        }

        @Override
        protected void saveUpdateTimeStamp(long timeStamp) {
            preferences.edit().putLong("timestamp_lastUpdate", timeStamp).apply();
        }

        @Override
        protected void parse(String jsonResponse) {
            parseNewsArticles(jsonResponse);
        }

        @Override
        protected void onPostExecute(Boolean hasTaskCompleted) {
            super.onPostExecute(hasTaskCompleted);

            if(hasTaskCompleted) {
                updateUI();
            } else {
                // Load old news
                String oldJSON = preferences.getString("NewsJsonCache", "-1");
                if(oldJSON != "-1") {
                    parse(oldJSON);
                    updateUI();
                }

                if(!((MainActivity)getActivity()).isNetworkAvailable(getContext()))
                    Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        }
    }
}