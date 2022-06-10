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

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfileImageProfile;
    TextView tvNameProfile;
    TextView tvScreenNameProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // instantiating elements from xml
        ivProfileImageProfile = findViewById(R.id.ivProfileImageProfile);
        tvNameProfile = findViewById(R.id.tvNameProfile);
        tvScreenNameProfile = findViewById(R.id.tvScreenNameProfile);

        String imageUrl = getIntent().getStringExtra("url");
        String name = getIntent().getStringExtra("name");
        String screenName = getIntent().getStringExtra("screen_name");

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