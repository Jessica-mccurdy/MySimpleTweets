package com.codepath.apps.restclienttemplate.models;


import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {



    public String body;
    public long uid;
    public User user;
    public String createdAt;




    // deserialize
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {


        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("full_text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        return tweet;
    }


}

