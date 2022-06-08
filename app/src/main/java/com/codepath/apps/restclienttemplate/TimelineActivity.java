package com.codepath.apps.restclienttemplate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    // tag to use for error readability in the log
    public static final String TAG = "TimelineActivity";

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    Button btnLogOut;
    SwipeRefreshLayout swipeContainer;

    ActivityResultLauncher<Intent> composeActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // creating a client to make the API call
        client = TwitterApp.getRestClient(this);

        // find the recycler view
        rvTweets = findViewById(R.id.rvTweets);

        // initialize the list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);

        // recycler view setup: layout manager
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        // recycler view setup: adapter
        rvTweets.setAdapter(adapter);

        populateHomeTimeline();

        // set up swipe to refresh functionality
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });

        // setting the progress bar colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // code to make newly created Tweets show up on the timeline
        composeActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // if the user comes back to this activity from ComposeActivity
                        // with no error or cancellation:
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            // get the data passed from ComposeActivity
                            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));

                            // adding this tweet to the list of Tweets
                            tweets.add(0, tweet);

                            // alerting the adapter so that the list is updated and displays the new Tweet
                            adapter.notifyItemInserted(0);

                            // making the transition smoother when a new Tweet is added
                            rvTweets.smoothScrollToPosition(0);
                        }
                    }
                });
    }

    public void fetchTimelineAsync(int page) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                adapter.clear();
                populateHomeTimeline();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("DEBUG", "Fetch timeline error: " + throwable.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu: adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.compose) {
            // the user has selected the compose icon
            // Toast.makeText(this, "Compose!", Toast.LENGTH_SHORT).show(); // for debugging

            // we should navigate from the timeline to the compose activity
            Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
            i.putExtra("stringToEdit", "CodePath");
            // startActivity(i);
            composeActivityResultLauncher.launch(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // this function will populate the timeline with Tweets we get from our Twitter API call
    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // function that executes when call is successful
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    e.printStackTrace();
                }

                Log.i(TAG, "onSuccess: " + json.toString());
            }

            // function that executes when call is unsuccessful
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure: " + response, throwable);
            }
        });
    }

    // logs out the user and brings them back to the login screen
    public void logOut(View view) {
        Log.i(TAG, "logOut");

        // forgetting the user
        TwitterApp.getRestClient(this).clearAccessToken();

        // creating a new intent to go back to the login screen
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above

        startActivity(i);
        finish();
    }
}