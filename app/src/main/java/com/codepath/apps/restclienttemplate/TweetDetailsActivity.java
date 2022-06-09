package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    // tag to use for error readability in the log
    public static final String TAG = "TweetDetailsActivity";

    Tweet tweet;

    // defining the elements in the layout
    ImageView ivProfileImageDetails;
    TextView tvBodyDetails;
    TextView tvScreenNameDetails;
    ImageView ivTweetImgDetails;
    TextView tvCreatedAtTimeDetails;
    TextView tvCreatedAtDateDetails;
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
        tvCreatedAtTimeDetails = findViewById(R.id.tvCreatedAtTimeDetails);
        tvCreatedAtDateDetails = findViewById(R.id.tvCreatedAtDateDetails);
        tvNameDetails = findViewById(R.id.tvNameDetails);

        ivProfileImageDetails = findViewById(R.id.ivProfileImageDetails);
        ivTweetImgDetails = findViewById(R.id.ivTweetImgDetails);

        // populating details page
        tvBodyDetails.setText(tweet.body);
        tvScreenNameDetails.setText("@" + tweet.user.screenName);
        tvNameDetails.setText(tweet.user.name);

        // getting the time when the Tweet was created
        tvCreatedAtTimeDetails.setText(tweet.createdAt.substring(11, 16));

        // getting the date when the Tweet was created
        tvCreatedAtDateDetails.setText(" \u2022 " + tweet.createdAt.substring(0, 10));

        // populating images
        // displaying the profile image using Glide
        Glide.with(this)
                .load(tweet.user.publicImageUrl)
                .transform(new RoundedCorners(400))
                .into(ivProfileImageDetails);

        // displaying the Tweet image IF it has an image using Glide
        if (!tweet.imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(tweet.imageUrl)
                    .transform(new RoundedCorners(35))
                    .into(ivTweetImgDetails);
            ivTweetImgDetails.setVisibility(View.VISIBLE);
        } else {
            ivTweetImgDetails.setVisibility(View.GONE);
        }
    }
}