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

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // instantiating elements from xml
        ivProfileImageProfile = findViewById(R.id.ivProfileImageProfile);
        tvNameProfile = findViewById(R.id.tvNameProfile);
        tvScreenNameProfile = findViewById(R.id.tvScreenNameProfile);

        // unwrapping the user from the parcel
        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));

        String imageUrl = user.publicImageUrl;
        String name = user.name;
        String screenName = user.screenName;

        // populating the profile screen
        tvNameProfile.setText(name);
        tvScreenNameProfile.setText("@" + screenName);

        // displaying the profile image using Glide
        Glide.with(this)
                .load(imageUrl)
                .transform(new RoundedCorners(400))
                .into(ivProfileImageProfile);
    }

    public void backFromProfile(View view) {
        Log.i("ProfileActivity", "backFromProfile");
        finish();
    }
}