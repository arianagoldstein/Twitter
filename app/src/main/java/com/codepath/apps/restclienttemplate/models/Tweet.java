package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public String imageUrl;
    public String id;
    public boolean isFavorited;
    public boolean isRetweeted;
    public int favoriteCount;
    public int retweetCount;

    // empty constructor needed by the Parceler library
    public Tweet() {

    }

    // constructs a Tweet given a JSON object
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("retweeted_status")) {
            return null;
        }
        // creates the Tweet and instantiates its member variables
        Tweet tweet = new Tweet();

        // if the Tweet is sufficiently long, make sure we load in the full text
        if (jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }

        // getting information from the JSON object that is returned
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getString("id_str");
        tweet.imageUrl = "";
        tweet.isFavorited = jsonObject.getBoolean("favorited");
        tweet.isRetweeted = jsonObject.getBoolean("retweeted");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");
        tweet.retweetCount = jsonObject.getInt("retweet_count");

        // accessing the media included in the Tweet by the user
        JSONObject entities = jsonObject.getJSONObject("entities");
        // Log.i("Tweet.java", entities.toString());

        // checking if the user has any media as part of their Tweet
        if (entities.has("media")) {
            JSONArray media = entities.getJSONArray("media");

            // checking if there are any photos
            if (media.length() != 0) {
                JSONObject img = media.getJSONObject(0);
                if (img.getString("type").equals("photo")) {
                    tweet.imageUrl = img.getString("media_url_https");
                }
            }
        }
        // returns the newly created Tweet
        return tweet;
    }

    // constructs a list of Tweets given a JSON array of tweets
    public static List<Tweet> fromJsonArray (JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        // converts each JSON object in the array to a Tweet object
        for (int i = 0; i < jsonArray.length(); i++) {
            Tweet tweetToAdd = fromJson(jsonArray.getJSONObject(i));
            if (tweetToAdd != null) {
                tweets.add(tweetToAdd);
            }
        }
        // returns the newly created list of Tweets
        return tweets;
    }

}
