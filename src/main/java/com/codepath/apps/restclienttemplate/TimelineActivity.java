package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    private final int REQUEST_CODE = 20;

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


        populateTimeline();
    }

    // creates a menu
    // creates action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onComposeAction(MenuItem mi) {
        // handle click here

        //where we will open new activity
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class); //changed
        i.putExtra("mode", 2); // pass arbitrary data to launched activity
        startActivityForResult(i, REQUEST_CODE);

    }


        // below needs to be edited TODO
    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String name = data.getExtras().getString("text");
            int code = data.getExtras().getInt("code", 0);
            // Use data parameter
            Tweet tweet = (Tweet) data.getSerializableExtra("text");
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
            // Toast the text to display temporarily on screen
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        }
    }

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

















