package com.vicki.bored.bored.joke;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.vicki.bored.bored.JokeWidget;
import com.vicki.bored.bored.R;
import com.vicki.bored.bored.movies.AlertDialogFragment;
import com.vicki.bored.bored.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JokeDisplayActivity extends AppCompatActivity {

    @BindView(R.id.jokeButton)
    Button JokeButton;

    @BindView(R.id.progressBar)
    public ProgressBar mProgress;

    @BindView(R.id.joke)
    public TextView mJokeTextView;

    public String BUNDLE_JOKE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);
        ButterKnife.bind(this);
       BUNDLE_JOKE =getResources().getString(R.string.bundle_joke);
        if(savedInstanceState!=null)
        {
            if (savedInstanceState.containsKey(BUNDLE_JOKE)) {
                String jokeRestore = savedInstanceState.getString(BUNDLE_JOKE);
                mJokeTextView.setText(jokeRestore);
            }
        }
        JokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgress.setVisibility(View.VISIBLE);

                if (NetworkUtils.isNetworkAvailable(getApplicationContext())){
                       new FetchJokes().execute();
                }
                else {
                    showError();
                }
            }
        });
    }
     public void updateWidget(String joke)
      {
        Context context = this;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.joke_widget);
        ComponentName thisWidget = new ComponentName(context, JokeWidget.class);
        remoteViews.setTextViewText(R.id.appwidget_text,joke);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

     }
     private void showError() {
        mProgress.setVisibility(View.INVISIBLE);
        AlertDialogFragment alert=new AlertDialogFragment();
        alert.show(getFragmentManager(),getResources().getString(R.string.alert));
     }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String lifecycleDisplayTextViewContents = mJokeTextView.getText().toString();
        outState.putString(BUNDLE_JOKE, lifecycleDisplayTextViewContents);
    }


    public class FetchJokes extends AsyncTask<String,String,String>
       {

        private final String JSON_URL = getResources().getString(R.string.joke_base_url);

        @Override
        protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        try {
            URL jokeURL=new URL(JSON_URL);
            urlConnection = (HttpURLConnection) jokeURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultJson;
    }

    @Override
    protected void onPostExecute(String resultJson) {
        super.onPostExecute(resultJson);
        JSONObject dataJsonObj = null;
        mProgress.setVisibility(View.INVISIBLE);
        String strJoke = "";

        try {
            dataJsonObj = new JSONObject(resultJson);
            JSONArray arrValue = dataJsonObj.getJSONArray("value");
            for (int i = 0; i < arrValue.length(); i++) {
                JSONObject arrValueObj = arrValue.getJSONObject(i);
                strJoke=arrValueObj.getString("joke");
            }
            updateWidget(strJoke);
            mJokeTextView.setText(strJoke);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

}
