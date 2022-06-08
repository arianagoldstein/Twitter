package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweets;

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
    public class ViewHolder extends RecyclerView.ViewHolder {

        // defining the elements in the layout
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetImg;
        TextView tvCreatedAt;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // instantiating the view objects with their corresponding elements in the layout
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImg = itemView.findViewById(R.id.ivTweetImg);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvName = itemView.findViewById(R.id.tvName);
        }

        public void bind(Tweet tweet) {
            // updating the elements with this Tweet's information
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            tvScreenName.setText("@" + tweet.user.screenName);

            // displaying the profile image using Glide
            Glide.with(context)
                    .load(tweet.user.publicImageUrl)
                    .transform(new RoundedCorners(400))
                    .into(ivProfileImage);

            // displaying the Tweet image IF it has an image
            if (!tweet.imageUrl.isEmpty()) {
                Glide.with(context).load(tweet.imageUrl).into(ivTweetImg);
                ivTweetImg.setVisibility(View.VISIBLE);
            }
            else {
                ivTweetImg.setVisibility(View.GONE);
            }

            // finding the time since the tweet was made and updating the createdAt textview
            String tweetTimeStr = tweet.createdAt; // date string from Tweet
            String timeToDisplay = getRelativeTimeAgo(tweetTimeStr); // finding difference in time
            tvCreatedAt.setText(timeToDisplay); // setting the text to display the string
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

    // function to get the time between when the tweet was posted and the current time
    // example String input: "Mon Apr 01 21:16:23 +0000 2014"
    public String getRelativeTimeAgo(String rawJsonDate) {
        // defining the Twitter date format
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

        // getting current time
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            // getting the time of the tweet
            long dateMillis = sf.parse(rawJsonDate).getTime();

            // instantiating the string to display
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // returning the string to display
        return relativeDate;
    }
}
