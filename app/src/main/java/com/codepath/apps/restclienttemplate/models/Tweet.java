package com.codepath.apps.restclienttemplate.models;

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

    // empty constructor needed by the Parceler library
    public Tweet() {

    }

    // constructs a Tweet given a JSON object
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        // creates the Tweet and instantiates its member variables
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        // returns the newly created Tweet
        return tweet;
    }

    // constructs a list of Tweets given a JSON array of tweets
    public static List<Tweet> fromJsonArray (JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        // converts each JSON object in the array to a Tweet object
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        // returns the newly created list of Tweets
        return tweets;
    }

}
