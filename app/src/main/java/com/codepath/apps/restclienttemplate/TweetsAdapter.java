package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // instantiating the view objects with their corresponding elements in the layout
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ivTweetImg = itemView.findViewById(R.id.ivTweetImg);
        }

        public void bind(Tweet tweet) {
            // updating the elements with this Tweet's information
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);

            // displaying the profile image using Glide
            Glide.with(context).load(tweet.user.publicImageUrl).into(ivProfileImage);

            // displaying the Tweet image IF it has an image
            if (!tweet.imageUrl.isEmpty()) {
                Glide.with(context).load(tweet.imageUrl).into(ivTweetImg);
                ivTweetImg.setVisibility(View.VISIBLE);
            }
            else {
                ivTweetImg.setVisibility(View.GONE);
            }
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
