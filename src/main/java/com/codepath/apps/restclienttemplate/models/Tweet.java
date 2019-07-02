package com.codepath.apps.restclienttemplate.models;


import org.json.JSONException;
import org.json.JSONObject;

//@Entity
public class Tweet {



    // Define database columns and associated fields
   // @PrimaryKey
   // @ColumnInfo
    public long uid;
    //@ColumnInfo
    //String user;
   // @ColumnInfo
    public String createdAt;
   // @ColumnInfo
    public String body;
    public User user;



    // Use @Embedded to keep the column entries as part of the same table while still
    // keeping the logical separation between the two objects.
    //@Embedded



    // deserialize
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {

        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }

/*

    // Add a constructor that creates an object from the JSON response
    public Tweet(JSONObject object){
        try {
            this.user = User.parseJSON(object.getJSONObject("user"));
            this.userHandle = object.getString("user_username");
            this.timestamp = object.getString("timestamp");
            this.body = object.getString("body");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweets.add(tweet);
        }

        return tweets;



    }

    */

}

