package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;

    // defining the elements in the layout
    ImageView ivProfileImageDetails;
    TextView tvBodyDetails;
    TextView tvScreenNameDetails;
    ImageView ivTweetImgDetails;
    TextView tvCreatedAtDetails;
    TextView tvNameDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        // unwrapping the Tweet from the Timeline activity
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        // accessing XML elements
        tvBodyDetails = findViewById(R.id.tvBodyDetails);
        tvScreenNameDetails = findViewById(R.id.tvScreenNameDetails);
        tvCreatedAtDetails = findViewById(R.id.tvCreatedAtDetails);
        tvNameDetails = findViewById(R.id.tvNameDetails);

        // populating details page
        tvBodyDetails.setText(tweet.body);
        tvScreenNameDetails.setText(tweet.user.screenName);
        tvCreatedAtDetails.setText(tweet.createdAt);
        tvNameDetails.setText(tweet.user.name);

        // populating images
    }
}