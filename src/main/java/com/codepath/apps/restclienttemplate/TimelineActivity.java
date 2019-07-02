package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = (TwitterClient) TwitterApp.getRestClient();

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
                                   // problem area!!!!!!!! TODO fix brackets


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

















