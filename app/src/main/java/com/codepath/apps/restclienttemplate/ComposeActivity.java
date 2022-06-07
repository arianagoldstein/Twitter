package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;

    // defining the elements in the layout
    EditText etCompose;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // instantiating the view objects with their corresponding elements in the layout
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        // setting a click listener for the button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the text the user typed in as a String
                String tweet = etCompose.getText().toString();

                // error checking: empty tweet
                if (tweet.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your Tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                // error checking: tweet is too long
                if (tweet.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your Tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }

                // displaying the user's tweet if it fits the requirements
                Toast.makeText(ComposeActivity.this, "Tweet: " + tweet, Toast.LENGTH_SHORT).show();


                // make an API call to Twitter to publish the Tweet
            }
        });


    }
}