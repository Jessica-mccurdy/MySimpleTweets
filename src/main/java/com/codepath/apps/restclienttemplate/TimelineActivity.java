package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

//import android.text.format.DateUtils;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private SwipeRefreshLayout swipeContainer;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    private final int REQUEST_CODE = 20;
    // Instance of the progress action-view
    MenuItem miActionProgressItem;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = (TwitterClient) TwitterApp.getRestClient(this);

        //find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // initialize the array list (datasource
        tweets = new ArrayList<>();
        //construct the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);

        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);
        rvTweets.scrollToPosition(0);

        rvTweets.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
                swipeContainer.setRefreshing(false);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        //Objects.requireNonNull(getActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));
        populateTimeline();

    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Remember to CLEAR OUT old items before appending in the new ones
                tweetAdapter.clear();
                // ...the data has come back, add new items to your adapter...
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }


    // creates a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);

        // Return to finish

        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }


    public void onComposeAction(MenuItem mi) {
        //where we will open new activity
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class); //changed
        i.putExtra("mode", 2); // pass arbitrary data to launched activity
        showProgressBar();
        startActivityForResult(i, REQUEST_CODE);


    }



    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            // get tweet
            Tweet tweet = (Tweet) Parcels.unwrap(getIntent().getExtras().getParcelable("tweet"));

            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
            hideProgressBar();

        }
    }

    //main method for loading up the view
    private void populateTimeline() {

        client.getHomeTimeline(new JsonHttpResponseHandler() {

                                   @Override
                                   public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                       //iterate through json array
                                       //for each entry deserialize the JSON object
                                       // convert each object to a Tweet model
                                       // convert each object to a Tweet Model
                                       // add that Tweet model to our data source
                                       // notify the adapter that we have added an item
                                       for (int i = 0; i < response.length(); i++) {
                                           try {
                                               Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                                               tweets.add(tweet);
                                               tweetAdapter.notifyItemInserted(tweets.size() - 1);
                                           } catch (JSONException e) {
                                               e.printStackTrace();
                                           }
                                       }

                                       //Log.d("TwitterClient", response.toString());
                                   }
                                   //


                                   @Override
                                   public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                       Log.d("TwitterClient", response.toString());
                                   }

                                   @Override
                                   public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                       Log.d("TwitterClient", responseString);
                                       throwable.printStackTrace();
                                   }

                                   @Override
                                   public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                       Log.d("TwitterClient", errorResponse.toString());
                                       throwable.printStackTrace();
                                   }

                                   @Override
                                   public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                       Log.d("TwitterClient", errorResponse.toString());
                                       throwable.printStackTrace();
                                   }
                               }
        );
    }





}

















