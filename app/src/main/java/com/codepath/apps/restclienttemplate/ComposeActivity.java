package com.codepath.apps.restclienttemplate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;

    // tag to use for error readability in the log
    public static final String TAG = "ComposeActivity";

    // defining the elements in the layout
    EditText etCompose;
    Button btnTweet;
    ImageView ivProfileImageCompose;

    TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // creating a client to make the API call
        client = TwitterApp.getRestClient(this);

        // instantiating the view objects with their corresponding elements in the layout
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);
        ivProfileImageCompose = findViewById(R.id.ivProfileImageCompose);

        String imageUrl = getIntent().getStringExtra("url");

        // displaying the profile image using Glide
        Glide.with(this)
                .load(imageUrl)
                .transform(new RoundedCorners(400))
                .into(ivProfileImageCompose);

        // setting a click listener for the button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the text the user typed in as a String
                String tweetContent = etCompose.getText().toString();

                // error checking: empty tweet
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your Tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                // error checking: tweet is too long
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your Tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }

                // displaying the user's tweet if it fits the requirements
                // Toast.makeText(ComposeActivity.this, "Tweet: " + tweetContent, Toast.LENGTH_SHORT).show();

                // make an API call to Twitter to publish the Tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "Successfully published Tweet!");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet: " + tweet);

                            // code that passes the new Tweet back to the timeline to be displayed
                            // prepare data intent
                            Intent data = new Intent();

                            // pass back the content of the tweet
                            data.putExtra("tweet", Parcels.wrap(tweet));

                            // activity finished ok, return the data
                            setResult(RESULT_OK, data); // set result code and bundle data for response
                            finish(); // closes the activity, pass data to parent

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "Error publishing Tweet.", throwable);
                    }
                });
            }
        });
    }

}