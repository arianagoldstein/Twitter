package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String name;
    public String screenName;
    public String publicImageUrl;

    // constructs a User given a JSON object
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        // creates the User and instantiates its member variables
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.publicImageUrl = jsonObject.getString("profile_image_url_https");

        // returns the newly created User
        return user;
    }
}
