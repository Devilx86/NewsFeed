package com.injectorx86.newsfeed.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.injectorx86.newsfeed.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

public abstract class JSONRequestTask extends AsyncTask<String, String, Boolean> {

    private String progressMessage;
    private ProgressDialog mProgressDialog;
    private Context mContext;


    public JSONRequestTask(Context context, String progressMessage) {
        this.mContext = context;
        this.progressMessage = progressMessage;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        if(!mProgressDialog.isShowing() && progressMessage != null) {
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(progressMessage);
            mProgressDialog.show();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String jsonResponse;

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(getURL()).openConnection();
            connection.connect();

            if(connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder strBuilder = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    strBuilder.append(line + "\n");
                }
                br.close();
                connection.disconnect();

                jsonResponse = strBuilder.toString();

                saveUpdateTimeStamp(Calendar.getInstance().getTimeInMillis());
                saveJsonResponse(jsonResponse);
                parse(jsonResponse);

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean hasTaskCompleted) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private String getURL() {

        String url = "https://newsapi.org/v2/everything?q=[keyword]&language=[lang]&apikey=[apikey]";
        String apiKey = mContext.getResources().getString(R.string.apiKey);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isShowEnglishOnly = preferences.getBoolean("News_ShowOnlyEnglishArticles", true);
        String searchKeyword = preferences.getString("News_SearchKeyword", "");

        // Key values
        Log.i("News_ShowOnlyEnglish..", ""+isShowEnglishOnly);
        Log.i("News_SearchKeyword", ""+isShowEnglishOnly);

        if(searchKeyword.isEmpty()) {
            url = url.replace("[keyword]", "android");
        } else {
            url = url.replace("[keyword]", searchKeyword);
        }

        if(isShowEnglishOnly) {
            url = url.replace("[lang]", "en");
        } else {
            url = url.replace("&language=[lang]", "");
        }

        url = url.replace("[apikey]", apiKey);
        Log.i("API_URL", url);

        return url;
    }

    protected void saveUpdateTimeStamp(long timestamp) {};
    protected void saveJsonResponse(String jsonResponse) {};
    protected abstract void parse(String jsonResponse);
}
