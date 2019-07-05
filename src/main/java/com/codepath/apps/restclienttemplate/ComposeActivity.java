package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {


    private TwitterClient client;
    EditText simpleEditText;
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = (TwitterClient) TwitterApp.getRestClient(this);

        simpleEditText = (EditText) findViewById(R.id.et_simple);

        tvCount = (TextView) findViewById(R.id.tvCount);
        tvCount.setText("280");

        simpleEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // this will show characters remaining
                tvCount.setText(280 - s.toString().length() + "/280");
            }
        });

        //call network for user details
        client.getVerifyCredentials(new JsonHttpResponseHandler() {

                                   @Override
                                   public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                                        Log.d("TwitterClient", response.toString());
                                   }


                                    // get user info
                                   @Override
                                   public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                       //getting json object
                                       //get info out of json object
                                       try {
                                           String name = response.getString("name");
                                           String userName = "@" + response.getString("screen_name");
                                           String profile_image_url = response.getString("profile_image_url_https");

                                           //Context context;
                                           // String imageUrl; //do i need this?
                                           ImageView ivProfileImage;
                                           TextView tvName;
                                           TextView tvScreenName;



                                           ivProfileImage = (ImageView) findViewById((R.id.ivProfileImage));
                                           tvName = (TextView) findViewById(R.id.tvName);
                                           tvScreenName = (TextView) findViewById(R.id.tvScreenName);

                                           //need to get own username and image
                                           tvName.setText(name);
                                           tvScreenName.setText(userName);


                                           // populate image
                                           Glide.with(getApplicationContext())
                                                   .load(profile_image_url)
                                                   .into(ivProfileImage);



                                       } catch (JSONException e) {
                                           e.printStackTrace();
                                       }


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
                               });


    }


    public void onExit(View view){
        Intent i = new Intent(ComposeActivity.this, TimelineActivity.class); //changed
        startActivity(i);
    }



    //need to add in a click response when button is pressed

    public void onSubmitTweet(View view){

        // get info from edit text
        String strValue = simpleEditText.getText().toString();

        // network call
        client.sendTweet(strValue, new JsonHttpResponseHandler() {


            // This is the important one where we have a success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //response is my tweet object

                // turn json into Tweet
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    // Prepare data intent
                    Intent data = new Intent();
                    // Pass relevant data back as a result
                    data.putExtra("tweet", Parcels.wrap(tweet));
                    data.putExtra("code", 200); // ints work too
                    // Activity finished ok, return the data
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish(); // closes the activity, pass data to parent
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray
                    response) {
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

        });

    }
}
