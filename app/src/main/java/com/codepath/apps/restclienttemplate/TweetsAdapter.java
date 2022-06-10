package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweets;

    // tag to use for error readability in the log
    public static final String TAG = "TweetsAdapter";

    // constants for relative time function
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    // constructor that instantiates the context and tweets member variables
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // for each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // creates a view for this specific tweet
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data at this position
        Tweet tweet = tweets.get(position);

        // bind the tweet with the viewholder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // defining the elements in the layout
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetImg;
        TextView tvCreatedAt;
        TextView tvName;
        ImageButton ibFavorite;
        TextView tvFavoriteCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // instantiating the view objects with their corresponding elements in the layout
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImg = itemView.findViewById(R.id.ivTweetImg);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvName = itemView.findViewById(R.id.tvName);
            ibFavorite = itemView.findViewById(R.id.ibFavorite);
            tvFavoriteCount = itemView.findViewById(R.id.tvFavoriteCount);

            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            // updating the elements with this Tweet's information
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvScreenName.setText("@" + tweet.user.screenName + " \u2022");
            tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

            // displaying the profile image using Glide
            Glide.with(context)
                    .load(tweet.user.publicImageUrl)
                    .transform(new RoundedCorners(400))
                    .into(ivProfileImage);

            // displaying the Tweet image IF it has an image
            if (!tweet.imageUrl.isEmpty()) {
                Glide.with(context)
                        .load(tweet.imageUrl)
                        .transform(new RoundedCorners(35))
                        .into(ivTweetImg);
                ivTweetImg.setVisibility(View.VISIBLE);
            } else {
                ivTweetImg.setVisibility(View.GONE);
            }

            // finding the time since the tweet was made and updating the createdAt textview
            String tweetTimeStr = tweet.createdAt; // date string from Tweet
            String timeToDisplay = getRelativeTimeAgo(tweetTimeStr); // finding difference in time
            tvCreatedAt.setText(timeToDisplay); // setting the text to display the string

            // setting up a click listener for the imageButton
            ibFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // user has clicked the like button
                    // we want to change the heart to be filled to indicate that it has been liked
                    if (!(tweet.isFavorited)) {
                        // making front-end look like we've liked the tweet
                        // changing the heart to be full
                        Drawable fullImg = context.getDrawable(R.drawable.fullheart);
                        // ibFavorite.setImageDrawable(fullImg);
                        ibFavorite.setBackground(fullImg);
                        tweet.isFavorited = true;
                        tweet.favoriteCount += 1;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

                        // telling Twitter that we've liked this Tweet
                        // creating a client to make the API call
                        TwitterApp.getRestClient(context).favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                // tweet should have been favorited
                                Log.i(TAG, "Tweet was favorited successfully");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                // tweet was not favorited
                                Log.e(TAG, "Tweet was NOT favorited successfully");
                            }
                        });
                    }
                    else {
                        // making front-end look like we've un-liked the tweet
                        // changing the heart to be empty
                        Drawable emptyImg = context.getDrawable(R.drawable.emptyheart);
                        // ibFavorite.setImageDrawable(emptyImg);
                        ibFavorite.setBackground(emptyImg);
                        tweet.isFavorited = false;
                        tweet.favoriteCount -= 1;
                        tvFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

                        // telling Twitter that we've un-liked this Tweet
                        // creating a client to make the API call
                        TwitterApp.getRestClient(context).unfavoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                // tweet should have been unfavorited
                                Log.i(TAG, "Tweet was unfavorited successfully");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                // tweet was not unfavorited
                                Log.e(TAG, "Tweet was NOT unfavorited successfully");
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            // get item position
            int position = getAdapterPosition();

            // check if the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // accessing movie at this position
                Tweet tweet = tweets.get(position);

                // create intent for the new activity
                Intent intent = new Intent(context, TweetDetailsActivity.class);

                // serialize the movie using Parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));

                // show the activity
                context.startActivity(intent);
            }
        }

        // function to get the time between when the tweet was posted and the current time
        // example String input: "Mon Apr 01 21:16:23 +0000 2014"
        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            try {
                long time = sf.parse(rawJsonDate).getTime();
                long now = System.currentTimeMillis();

                final long diff = now - time;
                if (diff < MINUTE_MILLIS) {
                    return "just now";
                } else if (diff < 2 * MINUTE_MILLIS) {
                    return "a minute ago";
                } else if (diff < 50 * MINUTE_MILLIS) {
                    return diff / MINUTE_MILLIS + " m";
                } else if (diff < 90 * MINUTE_MILLIS) {
                    return "an hour ago";
                } else if (diff < 24 * HOUR_MILLIS) {
                    return diff / HOUR_MILLIS + " h";
                } else if (diff < 48 * HOUR_MILLIS) {
                    return "yesterday";
                } else {
                    return diff / DAY_MILLIS + " d";
                }
            } catch (ParseException e) {
                Log.i(TAG, "getRelativeTimeAgo failed");
                e.printStackTrace();
            }

            return "";
        }
    }


    // functions for the swipe to refresh functionality
    // clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // add a list of items
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }
}
