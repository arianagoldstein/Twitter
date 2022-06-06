package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    // tag to use for error readability in the log
    public static final String TAG = "TimelineActivity";
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // creating a client to make the API call
        client = TwitterApp.getRestClient(this);
        populateHomeTimeline();
    }

    // this function will populate the timeline with Tweets we get from our Twitter API call
    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // function that executes when call is successful
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess" + json.toString());
            }

            // function that executes when call is unsuccessful
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure: " + response, throwable);
            }
        });
    }
}