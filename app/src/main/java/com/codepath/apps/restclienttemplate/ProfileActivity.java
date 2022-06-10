package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfileImageProfile;
    TextView tvNameProfile;
    TextView tvScreenNameProfile;
    TextView tvDescription;
    TextView tvFollowers;
    TextView tvFollowing;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // instantiating elements from xml
        ivProfileImageProfile = findViewById(R.id.ivProfileImageProfile);
        tvNameProfile = findViewById(R.id.tvNameProfile);
        tvScreenNameProfile = findViewById(R.id.tvScreenNameProfile);
        tvDescription = findViewById(R.id.tvDescription);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);

        // unwrapping the user from the parcel
        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));

        // populating the profile screen
        tvNameProfile.setText(user.name);
        tvScreenNameProfile.setText("@" + user.screenName);
        tvDescription.setText(user.description);
        tvFollowers.setText(user.followersCount + " followers");
        tvFollowing.setText(user.followingCount + " following");


        // displaying the profile image using Glide
        Glide.with(this)
                .load(user.publicImageUrl)
                .transform(new RoundedCorners(400))
                .into(ivProfileImageProfile);


        // adding followers and following list

    }

    public void backFromProfile(View view) {
        Log.i("ProfileActivity", "backFromProfile");
        finish();
    }
}