package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

public class ComposeActivity extends AppCompatActivity {




    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }





    //need to add in a click response when button is pressed

    public void onSubmitTweet(View view){

        // get info from edit text
        EditText simpleEditText = (EditText) findViewById(R.id.et_simple);
        String strValue = simpleEditText.getText().toString();

        // network call
        client.sendTweet(strValue, new JsonHttpResponseHandler() {

            // This is the important one where we have a success
            public void onSuccess(int statusCode, Header[] headers, JSONObject
                    response) {
                //response is my tweet object
                // turn json into Tweet
                Tweet tweet = new Tweet (response);


                // Prepare data intent
                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("tweet", Parcels.wrap(tweet));
                data.putExtra("code", 200); // ints work too
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent


            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray
                    response) {
                Log.d("TwitterClient", response.toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

        });

    }
}
