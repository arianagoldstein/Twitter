package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    public String name;
    public String screenName;
    public String publicImageUrl;
    public int followersCount;
    public int followingCount;
    public String profileBannerImageUrl;
    public String description;

    // empty constructor needed by the Parceler library
    public User() {

    }

    // constructs a User given a JSON object
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        // creates the User and instantiates its member variables
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.publicImageUrl = jsonObject.getString("profile_image_url_https");
        user.followersCount = jsonObject.getInt("followers_count");
        user.followingCount = jsonObject.getInt("friends_count");
        // user.profileBannerImageUrl = jsonObject.getString("profile_banner_url_https");
        user.description = jsonObject.getString("description");

        // returns the newly created User
        return user;
    }
}
